package com.example.minor_project.DTO;

import com.example.minor_project.model.Admin;
import com.example.minor_project.model.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CreateAdminRequest {

    @NotBlank(message = "name cannot be empty.")
    private String name;
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$"
    )
    @NotBlank(message = "Invalid email address.")
    private String email;
    @NotBlank(message = "username cannot be empty.")
    private String username;
    @NotBlank(message = "password cannot be empty.")
    private String password;


    public Admin to(){
        return Admin.builder()
                .email(this.email)
                .name(this.name)
                .user(
                    Users.builder()
                        .username(this.username)
                        .password(this.password)
                        .build()
                )
                .build();
    }
}
