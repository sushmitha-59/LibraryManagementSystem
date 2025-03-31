package com.example.minor_project.Service;

import com.example.minor_project.DTO.IssueTransactionRequest;
import com.example.minor_project.DTO.StudentResponse;
import com.example.minor_project.DTO.bookResponse;
import com.example.minor_project.Repository.StudentIdCacheRepo;
import com.example.minor_project.Repository.StudentRepo;
import com.example.minor_project.Repository.TransactionDao;
import com.example.minor_project.model.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    private StudentIdCacheRepo studentIdcacheRepo;

    @Autowired  TransactionDao transactionDao;
    @Autowired BookService bookService;
    @Autowired adminService adminservice;
    @Autowired StudentService studentService;

    @Value("${student.allowed.duration}")
    public Integer duration;

    @Value("${student.allowed.max-books}")
    public Integer maxBooks;

    @Value("${student.allowed.fine-amount-per-day}")
    public Integer fineAmount;

    /*
    * validate student,admin,book ids before issuing the book
    * check if the student is allowed , if she has taken more than allowed limit then we cant
    * check if book exists and not taken by anyone
    * if above everything is fine , then make entry in transaction table building new transaction object  as pending status and update the student , book
    */
    private Student  ValidateStudent(@NotBlank String studentId) throws Exception {
        try{
            return studentService.findStudentFromDb(Integer.parseInt(studentId));
        }
        catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }


    private Admin ValidateAdmin(@NotNull String adminId) {
        try{
            return adminservice.getAdminByIdEmail("id",adminId);
        } catch (Exception e) {
            return null;
        }
    }

    private Book ValidateBook(@NotNull String  id) throws Exception {
        List<Book> books=bookService.getBook("id",id);
        if(books.isEmpty()){
            throw new Exception("Invalid book Id");
        }else{
            return books.get(0);
        }
    }

    public String IssueType(IssueTransactionRequest issueTransactionRequest) throws Exception{
        //validate if student is valid
        Student student= ValidateStudent(issueTransactionRequest.getStudentId());
        if(student==null) {
            throw new Exception("Invalid Student Id");
        }
        //validate if admin is valid
        Admin admin= ValidateAdmin(issueTransactionRequest.getAdminId());
        if(admin==null){
            throw new Exception("invalid Admin Id");
        }

        //checking no. of books taken by student to check allowed limit
        if(student.getBooks().size() >= maxBooks){
            throw new Exception("Issue max_books limit reached for this student");
        }

        //checking if book is already taken by someone
        Book book= ValidateBook(issueTransactionRequest.getBookId());
        if(book.getStudent() !=null && book.getStudent().getId() !=null){
            throw new Exception("Book is already taken by " +  book.getStudent().getId());
        }

        //everything is fine,create new transaction and assign everything to it
        Transaction transaction=null;
        try{
            transaction=Transaction.builder()
                    .transactionStatus(MyTransactionStatus.PENDING)
                    .transactionType(TransactionType.ISSUE)
                    .student(student)
                    .admin(admin)
                    .book(book)
                    .TransactionId(UUID.randomUUID().toString())
                    .build();
            //making entry in the transaction table
            transactionDao.save(transaction);
            //update book to the student
            book.setStudent(student);
            //now update the transaction status to completed as we assigned the book to the student
            transaction.setTransactionStatus(MyTransactionStatus.SUCCESS);

            //delete the cache for the student
            studentIdcacheRepo.deleteStudent(Integer.parseInt(issueTransactionRequest.getStudentId()));
        }catch(Exception e){
            assert transaction !=null; //asset statements are believed to be true , correct assumptions , if it fails jvm throws error
            transaction.setTransactionStatus(MyTransactionStatus.FAILURE);
        }finally{ //after making changes to the Txn status we didn't save
            assert transaction !=null;
            transactionDao.save(transaction);
        }
        return transaction.getTransactionId();

    }/*
    for return type , we have to first validate the student , admin , book
    calculate fine,how we will calculate fine ? we have issue date , with that
    remove this student from the book own student
    update the transaction status
    */
    public String ReturnType(IssueTransactionRequest issueTransactionRequest) throws Exception {
        //validate if Student is valid
        System.out.println("Return type invoked");

        Student student= ValidateStudent(issueTransactionRequest.getStudentId());
        if(student==null) {
            throw new Exception("Invalid Student Id");
        }
        System.out.println("Student is : " + student.toString());
        //validate if admin is valid
        Admin admin= ValidateAdmin(issueTransactionRequest.getAdminId());
        if(admin==null){
            throw new Exception("invalid Admin Id");
        }
        System.out.println("Admin is : " + admin.toString());

        //validate book
        Book book= ValidateBook(issueTransactionRequest.getBookId());
        if(book ==null || book.getId()==null){
            throw new Exception("Invalid book");
        }
        System.out.println("book is : " + book.toString());

        //check if book is issued to student , if this condition is not checked then we can return book any number of times as it will return at least one issue record
        if(book.getStudent()==null){
            throw new Exception("Book is already returned by " + student.getId().toString());
        }
        //get the issuance date to calculate fine
        //findTransactionByStudent... is similar to findByStudent...
        Transaction issueTransaction=null;
        try{
            issueTransaction=transactionDao.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(student, book, TransactionType.ISSUE);
        } catch (Exception e) {
            throw new Exception("Issuance record not found");
        }

        //build the transaction to save into the table
        Transaction transaction=null;
        try{
            transaction=Transaction.builder()
                    .fine(fineCalculation(issueTransaction.getCreatedOn()))
                    .transactionType(TransactionType.RETURN)
                    .transactionStatus(MyTransactionStatus.PENDING)
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .TransactionId(UUID.randomUUID().toString())
                    .build();
            transactionDao.save(transaction);

            //unassign book to student ,and update the book in the database
            book.setStudent(null);
            bookService.CreateOrUpdateBook(book);

            //update the transaction status to success
            transaction.setTransactionStatus(MyTransactionStatus.SUCCESS);
            //delete the cache for the student
            studentIdcacheRepo.deleteStudent(Integer.parseInt(issueTransactionRequest.getStudentId()));
        } catch (Exception e) {
            assert transaction !=null;
            transaction.setTransactionStatus(MyTransactionStatus.FAILURE);
        }
        finally{ //after making changes to the Txn status we didn't save
            assert transaction !=null;
            transactionDao.save(transaction);
        }

        return transaction.getTransactionId();

    }

    private Integer fineCalculation(Date createdOn) {
        long CreatedOninmilliseconds=createdOn.getTime();
        long currentTime=System.currentTimeMillis();
        long diff=currentTime - CreatedOninmilliseconds;
        long daysPassed= TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
        if(daysPassed >= duration){
            return (int) ((daysPassed-duration)*fineAmount);
        }
        return 0;
    }

    public String initiateTxn(@Valid IssueTransactionRequest issueTransactionRequest) throws Exception {
        System.out.println(" Transaction type is  : " + issueTransactionRequest.getTransactionType());
           return issueTransactionRequest.getTransactionType() == TransactionType.ISSUE ? IssueType(issueTransactionRequest):ReturnType(issueTransactionRequest);
    }
}
