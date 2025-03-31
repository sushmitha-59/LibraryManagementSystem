package com.example.minor_project.model;

import com.example.minor_project.DTO.StudentResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.util.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity @Builder
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id; //this will be the primary key
    //student:book==>1:n , we have to declare list of books here to store those n quantities

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude    private List<Book> books;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude    private List<Transaction> transactions;

    //one to one mapping with user table
    @OneToOne
    @JoinColumn
    @ToString.Exclude    private Users user;

    private Integer age;
    private String name;
    @Column(unique = true)
    private String rollNumber;

    @Column(unique = true) //if we don't want duplicate emails in table , we can mention this
    private String email;  //it doesn't mean that this is primary key
    @CreationTimestamp
    private Date CreatedOn;
    @UpdateTimestamp
    private Date UpdatedOn;

    //model to response dto conversion
    public StudentResponse to(){
        Map<Integer,String> books2=new HashMap<>();
        if(this.getBooks() !=null){
            for(Book Book:this.getBooks()){
                books2.put(Book.getId(),Book.getName());
            }
        }
        Map<String, String> txn2=new HashMap<>();
        if(this.getTransactions() !=null){
            for(Transaction txn : this.getTransactions()){
                txn2.put(txn.getTransactionId(), String.valueOf(txn.getTransactionStatus()));
            }
        }
        return StudentResponse.builder()
                .age(this.age)
                .userId(this.user.getId())
                .books(books2)
                .roll_number(this.rollNumber)
                .CreatedOn(this.CreatedOn)
                .email(this.email)
                .id(this.id)
                .name(this.name)
                .transactions(txn2)
                .UpdatedOn(this.UpdatedOn)
                .build();
    }

    @Override
    public String toString() {
        Map<Integer, String> books2 = new HashMap<>();
        if (this.getBooks() != null) {
            for (Book book : this.getBooks()) {
                if (book != null) {
                    books2.put(book.getId(), book.getName());
                }
            }
        }

        Map<String, String> txn2 = new HashMap<>();
        if (this.getTransactions() != null) {
            for (Transaction txn : this.getTransactions()) {
                if (txn != null) {
                    txn2.put(txn.getTransactionId(), String.valueOf(txn.getTransactionStatus()));
                }
            }
        }

        String userId = (this.user != null) ? String.valueOf(this.user.getId()) : "null";


        return "Student { " +
                "id: " + this.id +
                ", age: " + this.age +
                ", name: '" + this.name + '\'' +
                ", rollNumber: '" + this.rollNumber + '\'' +
                ", email: '" + this.email + '\'' +
                ", user: " + userId +
                ", books: " + books2 +
                ", Transactions: " + txn2 +
                '}';
    }
}