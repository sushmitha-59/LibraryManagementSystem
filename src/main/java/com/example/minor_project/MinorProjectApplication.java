package com.example.minor_project;

import com.example.minor_project.Repository.AdminRepo;
import com.example.minor_project.Service.CustomUserDetailsService;
import com.example.minor_project.Utilities.Constants;
import com.example.minor_project.model.Admin;
import com.example.minor_project.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinorProjectApplication implements CommandLineRunner {

	@Autowired
	public AdminRepo adminRepo;
	@Autowired
	public CustomUserDetailsService customUserDetailsService;

	@Value("${Admin.user.username}")
	private String Username;

	@Value("${Admin.user.password}")
	private String password;
	@Value("${Admin.user.email}")
	private String email;


	public static void main(String[] args) {
		SpringApplication.run(MinorProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Admin admin= adminRepo.findByEmail(this.email);

		if(admin ==null){
			admin=Admin.builder()
				.email(this.email)
				.name(this.Username)
				.user(
					Users.builder()
						.username(this.Username)
						.password(this.password)
						.build()
				)
				.build();

			Users user=customUserDetailsService.SaveUser(Constants.ADMIN_USER,admin.getUser());
			admin.setUser(user);
			adminRepo.save(admin);
		}
	}
//for every application there should be an admin
// spring will create above admin if not present , after creating every beans
}
