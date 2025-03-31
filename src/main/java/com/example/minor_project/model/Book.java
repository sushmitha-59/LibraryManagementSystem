package com.example.minor_project.model;
import com.example.minor_project.DTO.bookResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity @Builder
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @CreationTimestamp //annotations for created on date , it will set creation time here
    private Date createdOn;
    @Column(unique = true,nullable = false)
    private String name;
    @UpdateTimestamp
    private Date updatedOn;
    @Enumerated(value=EnumType.STRING)
    private genre_enum genre;//genre we give ,should be in the list of this enum
    private Integer price;
    //select * from book where student_id=<>;
    //book:student=n:1==>one student object we have to declare here
    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private Student student;
    //select * from book where author_id=<>;
    //book to author is n:1 ,one author can write multiple books ,so we should mention
    // author object here to tell to which book class is forming relationship
    @ManyToOne
    @JoinColumn
    private Author author;
    //select * from transaction where book_id=<>;
    //book:transaction=n:1
    @OneToMany(mappedBy = "book")
    private List<Transaction> transactions;
    public bookResponse to(){
        return bookResponse.builder()
                .authorName(this.author !=null ? author.getName() : null)
                .authorId(this.author !=null ? author.getId() : null)
                .id(this.id)
                .transactions(this.transactions)
                .genre(this.genre)
                .name(this.name)
                .price(this.price)
                .student(this.student)
                .createdOn(this.createdOn)
                .updatedOn(this.updatedOn)
                .build();
    }
    @Override
    public String toString() {
        return "Book {"+
                "id :"+this.id+"\"" +
                " ,genre: "+this.genre+"\"" +
                " ,name : "+this.name+"\"" +
                " ,student : "+this.student+"\"" +
                " ,author : "+this.author+"\"" +
                " ,transactions : "+this.transactions+"\"" +
                "}";
    }
}
