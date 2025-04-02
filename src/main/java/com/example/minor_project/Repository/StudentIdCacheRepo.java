package com.example.minor_project.Repository;

import com.example.minor_project.DTO.StudentResponse;
import com.example.minor_project.Utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class StudentIdCacheRepo {
    @Autowired
    RedisTemplate<String, Object> redistemplate;

    //helper function to store student in one format , so that we can retrieve it easily
    public String studentSetGetFormat(Integer id) {
        return Constants.STUDENT_PREFIX + id;
    }

    //set students in the redis
    public void setStudent(StudentResponse student) {
        this.redistemplate.opsForValue().set(
                studentSetGetFormat(student.getId()),
                student,
                Constants.STUDENT_CACHE_EXPIRY,
                TimeUnit.MINUTES
        );
    }

    //get student if data is there in cache
    public StudentResponse getStudent(Integer id) {
        return (StudentResponse) this.redistemplate.opsForValue().get(
                studentSetGetFormat(id)
        );
    }

    public Boolean deleteStudent(Integer id) {
        return this.redistemplate.delete(studentSetGetFormat(id));
    }
}
