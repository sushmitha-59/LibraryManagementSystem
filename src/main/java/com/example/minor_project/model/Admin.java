package com.example.minor_project.model;

import com.example.minor_project.DTO.AdminResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity @ToString
public class Admin {
    @Id //every table should have primary key , even if you don't need it
    @GeneratedValue(strategy= GenerationType.IDENTITY) //identity will be of integer type
    private Integer id; //this will be the primary key

    private String name;

    @Column(unique = true ,nullable = false) //if we don't want duplicate emails in table , we can mention this
    private String email;  //it doesn't mean that this is primary key

    //admin:transaction = 1:n , so create a list to save those n transactions
    //we have to give variable we are using in transaction table for admin object for mapping
    @OneToMany(mappedBy = "admin")
    private List<Transaction> transaction_list;

    @OneToOne
    @JoinColumn
    private Users user;

    @CreationTimestamp
    private Date CreatedOn; //CreationTimestamp generates the time of creation of object
    @UpdateTimestamp
    private Date UpdatedOn; //whenever we update happens ,this time will get updated

    public AdminResponse to() {
        return AdminResponse.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .CreatedOn(this.CreatedOn)
                .UpdatedOn(this.UpdatedOn)
                .build();
    }
}
