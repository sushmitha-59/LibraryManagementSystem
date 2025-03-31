package com.example.minor_project.DTO;

import com.example.minor_project.model.Student;
import com.example.minor_project.model.Users;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder @ToString
public class CreateStudentRequest {
    @Min(value = 10,message = "Age should be more than 10")
    private Integer age;
    @NotBlank(message = "Name should not be blank.")
    private String name;
    @NotBlank(message = "rollNumber should not be blank.")
    private String rollNumber;
    @NotBlank(message = "password should not be blank.")
    private String password;
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$",
            message = "Invalid email format. Please enter a valid Gmail address ending with @gmail.com"
    )
    @NotBlank(message = "Email should not be blank.")
    private String email;
    @NotBlank(message = "UserName should not be blank.")
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
