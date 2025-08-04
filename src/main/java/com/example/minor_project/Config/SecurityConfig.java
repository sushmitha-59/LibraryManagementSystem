package com.example.minor_project.Config;

import com.example.minor_project.Service.CustomUserDetailsService;
import com.example.minor_project.Utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  //add dependency spring-boot-starter-security
public class SecurityConfig {

    //    @Bean
//    public UserDetailsService CustomDetailsService() {
//        return new CustomUserDetailsService();
//    }
    //when we are working with jwt , we need to implement authentication Manager
//    @Bean
//    public UserDetailsService customUserDetailsService() {
//        return new CustomUserDetailsService(); // no autowiring here
//    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //BCryptPasswordEncoder is the hashing algorithm which implements the interface PasswordEncoder
    }

    //we have to tell DaoAuthenticationProvider to which service it should use
    @Bean
    public AuthenticationProvider AuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }

    //security filter chain for adding security rules
    //disabling csrf , enabling basic and form login
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/", "/login", "/oauth2/**", "/registerStudent").permitAll()
                                .requestMatchers("/student/info").hasAuthority(Constants.STUDENT_SELF_INFO_AUTHORITY)
                                .requestMatchers("/admin/**").hasAuthority(Constants.CREATE_ADMIN)
                                .requestMatchers("/book/create", "/book/delete/{id}").hasAuthority(Constants.CREATE_BOOK)
                                .requestMatchers("/book/get", "/book/getAll").hasAnyAuthority(Constants.STUDENT_BOOKS_VIEW, Constants.ADMIN_BOOKS_VIEW)
                                .requestMatchers("/student/listAll", "/student/search", "/student/search2", "/student/delete/**", "/student/update/**").hasAuthority(Constants.ADMIN_AUTHORITY_FOR_STUDENT)
                                .requestMatchers(HttpMethod.POST, "/student/create").permitAll()  //for only authenticated people
                                .requestMatchers("/transaction/initiate").hasAuthority(Constants.INITIATE_TRANSACTION)
                                .requestMatchers("/csv/upload").hasAuthority(Constants.UPLOAD_CSV)
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)//telling spring to go through this filter before default filter class i.e UsernamePasswordAuthenticationFilter
                .build();
    }
}
