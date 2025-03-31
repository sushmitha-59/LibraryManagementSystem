package com.example.minor_project.model;

import com.example.minor_project.DTO.StudentResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity @Builder
@Table(name="students")
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id; //this will be the primary key
    //student:book==>1:n , we have to declare list of books here to store those n quantities
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Book> books;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    //one to one mapping with user table
    @OneToOne
    @JoinColumn
    private Users user;

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
        return StudentResponse.builder()
                .age(this.age)
                .books(this.books)
                .roll_number(this.rollNumber)
                .CreatedOn(this.CreatedOn)
                .email(this.email)
                .id(this.id)
                .name(this.name)
                .transactions(this.transactions)
                .UpdatedOn(this.UpdatedOn)
                .build();
    }

    @Override
    public String toString() {
        return "student {"+
                "id :"+this.id+"\"" +
                " ,age : "+this.age+"\"" +
                " ,name : "+this.name+"\"" +
                " ,rollNumber : "+this.rollNumber+"\"" +
                " ,email : "+this.email+"\"" +
                " ,user : "+this.user.getId()+"\"" +
                "}";
    }
}
