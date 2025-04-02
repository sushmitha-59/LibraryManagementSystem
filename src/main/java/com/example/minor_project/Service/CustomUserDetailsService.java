package com.example.minor_project.Service;

import com.example.minor_project.Repository.UserRepo;
import com.example.minor_project.Utilities.AuthoritiesProvider;
import com.example.minor_project.Utilities.Constants;
import com.example.minor_project.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userrepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userrepo.findByUsername(username);
    }

    public Users SaveUser(String userType, Users user) {
        //first hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //set the authorities for the corresponding usertype

        String authorities = AuthoritiesProvider.getAuthorities(userType);
        if (authorities == null) {
            throw new RuntimeException("Authorities are null");
        }
        if (authorities.equals(Constants.INVALID_USER)) {
            throw new RuntimeException("Invalid for userType " + userType);
        }

        user.setAuthorities(authorities);
        return userrepo.save(user);
    }
}
