package com.example.minor_project.Service;
import com.example.minor_project.DTO.StudentResponse;
import com.example.minor_project.Repository.StudentIdCacheRepo;
import com.example.minor_project.Repository.StudentRepo;
import com.example.minor_project.Repository.TransactionDao;
import com.example.minor_project.Repository.UserRepo;
import com.example.minor_project.Utilities.Constants;
import com.example.minor_project.model.Book;
import com.example.minor_project.model.Student;
import com.example.minor_project.model.Users;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private CustomUserDetailsService CustomUserDetailsService;

    @Autowired
    private StudentIdCacheRepo studentIdcacheRepo;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private UserRepo userDao;

    public Student createStudent(Student student) throws Exception{
        try {
            //we already built the user object in the student DTO
            Users user=student.getUser();
            //set the authorities to the above user
            user=CustomUserDetailsService.SaveUser(Constants.STUDENT_USER,user);
            //check if the user is valid
            if(user.getId()==null){
                throw new RuntimeException(Constants.INVALID_USER);
            }

            //update the user details  in the student object
            student.setUser(user);

            //update the student table
            return studentRepo.save(student);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> getALlStudents() {
        return studentRepo.findAll();
    }

    //helper function to get student from cache , if not present in cache then DB
    private Student findStudentByID(@NotBlank Integer id) throws Exception {
        Student student=studentIdcacheRepo.getStudent(id);
        if(student !=null){
            System.out.println("Getting Student from redis");
            return student;
        }
        else{
            try{
                Optional<Student> stu=studentRepo.findById(id);
                if(stu.isPresent()){
                    student=stu.get();
                    System.out.println("Getting Student from DB");
                    studentIdcacheRepo.setStudent(student);
                    return student;
                }
                else{
                    throw new Exception("Student with id "+id+"  not found ");
                }
            }catch(Exception e){
                throw new Exception("Student with id "+id+"  not found "+ e.getMessage());
            }
        }
    }


    public Student searchStudentByIdEmailRoll(@NotBlank String searchKey, @NotBlank String searchValue) throws Exception {
        switch (searchKey) {
            case "id" :{
                Student student=findStudentByID(Integer.parseInt(searchValue));
                if(student==null ||(student !=null && student.getId()==null)){
                    throw new Exception("Invalid search value");
                }
                return student;
            }
            case "email" :
                return studentRepo.findByEmail(searchValue);
            case "rollNumber" :
                return studentRepo.findByRollNumber(searchValue);
            default:
                try {
                    throw new Exception("Invalid search key");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
    }

    public List<Student> searchStudentByNameAge(@NotBlank String searchKey, @NotBlank String searchValue) throws Exception {
        if(searchKey.equalsIgnoreCase("name")){
            List<Student> stu=studentRepo.findByName(searchValue);
            if(stu.isEmpty()){
                throw new Exception("Users with  name:\" "+searchValue+"\"  not Found");
            }
            return stu;
        } else if (searchKey.equalsIgnoreCase("age")) {
            List<Student> stu=studentRepo.findByAge(Integer.parseInt(searchValue));
            if(stu.isEmpty()){
                throw new Exception("Users with  age: \""+searchValue+"\" not Found");
            }
            return stu;
        }
        else{
            throw new Exception("Invalid key for searchStudentByNameAge method");
        }
    }

    @Transactional
    public void deleteStudent(Integer id) throws Exception {
        //check if the student is valid or not
        Student stu=findStudentByID(id);
        System.out.println("Student is " + stu.toString());
        if(stu.getId() != null){
            List<Book> books=stu.getBooks();
            System.out.println("books by the student are " + stu.getBooks().toString());
            if(!books.isEmpty()){
                throw new Exception("Student is associated with books, please Return the books");
            }
            System.out.println("not books issue");
            //delete all the related transactions
            if(! stu.getTransactions().isEmpty() ){
                transactionDao.deleteByStudentId(id);
            }
            System.out.println("not transactions issue");
            //delete user entry
            userDao.deleteById(stu.getUser().getId());
            System.out.println("not users issue");
            studentRepo.deleteById(id);
            //delete from cache if exists
            studentIdcacheRepo.deleteStudent(id);
        }
        else {
            throw new Exception("Cannot find the student");
        }
    }

    public StudentResponse updateStudent(Integer id, String key, String value) throws Exception {
        Student stu=findStudentByID(id);
        System.out.println("Student is " + stu.toString());
        if(stu.getId() != null){
            switch (key){
                case "age":
                    stu.setAge(Integer.parseInt(value));
                    break;
                case "email":
                    stu.setEmail(value);
                    break;
                case "name":
                    stu.setName(value);
                    break;
                default:
                    throw new Exception("Update for this field is not Allowed");
            }
            //save the student
            studentRepo.save(stu);
            studentIdcacheRepo.setStudent(stu); //save in the cache ,probability of them getting this data will be more to verify
            return stu.to();
        }
        else {
            throw new Exception("Cannot find the student");
        }
    }
}
