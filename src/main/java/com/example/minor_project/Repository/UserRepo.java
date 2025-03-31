package com.example.minor_project.Repository;

import com.example.minor_project.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<Users, Integer> {
     Users findByUsername(String username);

    void deleteByUsername(String username);
}
