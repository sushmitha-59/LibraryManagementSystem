package com.example.minor_project.Config;

import com.example.minor_project.Service.CustomUserDetailsService;
import com.example.minor_project.Utilities.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  //add dependency spring-boot-starter-security
public class SecurityConfig {

    @Bean
    public UserDetailsService CustomDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //BCryptPasswordEncoder is the hashing algorithm which implements the interface PasswordEncoder
    }

    //we have to tell DaoAuthenticationProvider to which service it should use
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(CustomDetailsService());
        return daoAuthenticationProvider;
    }

    //security filter chain for adding security rules
    //disabling csrf , enabling basic and form login
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
            auth -> auth
                    .requestMatchers("/student/info").hasAuthority(Constants.STUDENT_SELF_INFO_AUTHORITY)
                    .requestMatchers("/admin/**").hasAuthority(Constants.CREATE_ADMIN)
                    .requestMatchers("/book/create", "/book/delete/{id}").hasAuthority(Constants.CREATE_BOOK)
                    .requestMatchers("/book/get", "/book/getAll").hasAuthority(Constants.READ_BOOK)
                    .requestMatchers("/student/listAll", "/student/search", "/student/search2", "/student/delete/**", "/student/update/**").hasAuthority(Constants.ADMIN_AUTHORITY_FOR_STUDENT)
                    .requestMatchers(HttpMethod.POST, "/student/create").permitAll()  //for only authenticated people
                    .requestMatchers("/transaction/initiate").hasAuthority(Constants.INITIATE_TRANSACTION)
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }
}
