package com.example.minor_project.Repository;

import com.example.minor_project.model.Book;
import com.example.minor_project.model.genre_enum;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    //in the book model we have name , genre variables so we can use direct findByName functions
    List<Book> findByName(String name);
    List<Book> findByGenre( genre_enum genre);

    //in the book model we just have author object not author_name , so we can search with entire author object
    //not just with only author_name , so we have to write custom query to handle this , ?1 takes first parameter
    @Query("select b from Book b , Author e where b.author.id=e.id and e.name= ?1")
    List<Book> findByAuthor_name( String authorName);

    default String deleteBook(Integer id){
        try{
            deleteById(id);
            return "Book got deleted successfully" ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
