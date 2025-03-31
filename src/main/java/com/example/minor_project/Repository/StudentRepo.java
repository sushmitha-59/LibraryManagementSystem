package com.example.minor_project.Repository;

import com.example.minor_project.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student,Integer> {
    public Optional<Student> findById(Integer id);
    public  List<Student> findByName(String name);
    public List<Student> findByAge(Integer age);
    public  Student findByEmail(String email);
    public  Student findByRollNumber(String roll);
}
