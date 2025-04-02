package com.example.minor_project.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //author:book=1:n ,in book we have given author object and mapped column
    //here we have to use list to store those n books
    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private List<Book> books;

    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @CreationTimestamp
    private Date CreatedOn;
    @UpdateTimestamp
    private Date UpdatedOn;

    @Override
    public String toString() {
        Map<Integer, String> books2 = new HashMap<>();
        if (this.getBooks() != null) {
            for (Book Book : this.getBooks()) {
                books2.put(Book.getId(), Book.getName());
            }
        }
        return "author {" +
                "id :" + this.id + "\"" +
                " ,name : " + this.name + "\"" +
                " ,email : " + this.email + "\"" +
                " ,books : " + books2.toString() + "\"" +
                "}";
    }
}
