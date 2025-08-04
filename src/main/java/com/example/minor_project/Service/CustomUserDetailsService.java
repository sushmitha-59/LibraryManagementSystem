package com.example.minor_project.Service;

import com.example.minor_project.Repository.UserRepo;
import com.example.minor_project.Utilities.AuthoritiesProvider;
import com.example.minor_project.Utilities.Constants;
import com.example.minor_project.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//@Configuration
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userrepo;

    @Autowired
    JWTservice jwTservice;

    @Autowired
    private AuthenticationConfiguration config;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userrepo.findByUsername(username);
    }

    public Users SaveUser(String userType, Users user) {
        //first hash the password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        //set the rawauthorities for the corresponding usertype
        String authorities = AuthoritiesProvider.getAuthorities(userType);

        if (authorities == null) {
            throw new RuntimeException("Authorities are null");
        }
        if (authorities.equals(Constants.INVALID_USER)) {
            throw new RuntimeException("Invalid for userType " + userType);
        }

        user.setRawauthorities(authorities);
        return userrepo.save(user);
    }

    //manually writing the code to authenticate the user
    public String verify(Users user) throws Exception {
        Authentication authentication=config.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated()){
            return jwTservice.generateToken(user);
        }
        return "Failure";
    }
}
