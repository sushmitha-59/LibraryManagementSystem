package com.example.minor_project.Repository;

import com.example.minor_project.model.Book;
import com.example.minor_project.model.Student;
import com.example.minor_project.model.Transaction;
import com.example.minor_project.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends JpaRepository<Transaction, Integer> {
    Transaction findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(Student student,
                                                                       Book book,
                                                                       TransactionType transactionType);

    @Modifying
    @Query(value = "DELETE FROM transaction WHERE student_id = :studentId", nativeQuery = true)
    void deleteByStudentId(@Param("studentId") Integer studentId);
}
