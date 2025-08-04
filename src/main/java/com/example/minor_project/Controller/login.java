package com.example.minor_project.Controller;

import com.example.minor_project.DTO.CreateStudentRequest;
import com.example.minor_project.Service.CustomUserDetailsService;
import com.example.minor_project.Service.StudentService;
import com.example.minor_project.model.Student;
import com.example.minor_project.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class login {
//    @GetMapping("/")
//    public String redirectLogin(){
//        return "login";
//    }
    @Autowired
    StudentService studentService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @ResponseBody
    @PostMapping("/registerStudent")
    public Student registerStudent(@RequestBody CreateStudentRequest createStudentRequest) throws Exception {
        return studentService.createStudent(createStudentRequest.to());
    }
    @ResponseBody
    @PostMapping("/verify")
    public String verifyStudent(@RequestBody Users user){
        try{
            return customUserDetailsService.verify(user);
        }catch(Exception e){
            return e.getCause()==null?e.getMessage():e.getCause().getLocalizedMessage();
        }
    }
}
