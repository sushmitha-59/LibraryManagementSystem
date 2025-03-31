package com.example.minor_project.DTO;

import com.example.minor_project.model.Student;
import com.example.minor_project.model.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder @ToString
public class CreateStudentRequest {
    private Integer age;
    @NotBlank
    private String name;
    @NotBlank
    private String rollNumber;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    public Student to(){
        return Student.builder()
                .age(this.age)
                .name(this.name)
                .user( //building user object also
                        Users.builder()
                                .password(this.password)
                                .username(this.username)
                                .build()
                )
                .rollNumber(this.rollNumber)
                .email(this.email)
                .build();
    }
}
