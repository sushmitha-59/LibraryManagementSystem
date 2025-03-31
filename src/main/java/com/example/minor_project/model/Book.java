package com.example.minor_project.model;
import com.example.minor_project.DTO.bookResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity @Builder
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
    @ToString.Exclude
    private Student student;
    //select * from book where author_id=<>;
    //book to author is n:1 ,one author can write multiple books ,so we should mention
    // author object here to tell to which book class is forming relationship
    @ManyToOne
    @ToString.Exclude    @JoinColumn
    private Author author;
    //select * from transaction where book_id=<>;
    //book:transaction=n:1
    @OneToMany(mappedBy = "book")
    private List<Transaction> transactions;
    public bookResponse to(){
        Map<String, String> txn2=new HashMap<>();
        if(this.getTransactions() !=null){
            for(Transaction txn : this.getTransactions()){
                txn2.put(txn.getTransactionId(), String.valueOf(txn.getTransactionStatus()));
            }
        }
        return bookResponse.builder()
                .authorName(this.author !=null ? author.getName() : null)
                .authorId(this.author !=null ? author.getId() : null)
                .id(this.id)
                .transactions(txn2)
                .genre(this.genre)
                .name(this.name)
                .price(this.price)
                .studentId(this.student!=null?this.student.getId():null)
                .createdOn(this.createdOn)
                .updatedOn(this.updatedOn)
                .build();
    }
    @Override
    public String toString() {
        Map<String, String> txn2=new HashMap<>();
        if(this.getTransactions() !=null){
            for(Transaction txn : this.getTransactions()){
                txn2.put(txn.getTransactionId(), String.valueOf(txn.getTransactionStatus()));
            }
        }
        return "Book {"+
                "id :"+this.id+"\"" +
                " ,genre: "+this.genre+"\"" +
                " ,name : "+this.name+"\"" +
                " ,student_id : "+this.student.getId()+"\"" +
                " ,author_id : "+this.author.getId()+"\"" +
                " ,transactions : "+txn2.toString()+"\"" +
                "}";
    }
}
