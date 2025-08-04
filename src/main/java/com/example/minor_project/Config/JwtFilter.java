package com.example.minor_project.Config;

import com.example.minor_project.Service.CustomUserDetailsService;
import com.example.minor_project.Service.JWTservice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Configuration
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JWTservice jwTservice;

    @Autowired
    ApplicationContext context; //with context we can get the beans instead of autowiring that bean

    //to make a class  filter either we have to annotate it with filter annotations id exists or extend another filter class
    //we don't have filter annotation as such , so we have to extend from filter class
    //OncePerRequestFilter is a class which gets called per every request once
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //here in the request header this authrization bearer token will be there , get thet
        String authHeader=request.getHeader("Authorization");
        String jwtToken="";
        //if the authheader is null means login time then generates valid token and then default filter chain
        //so no need to validate token
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            //Authorization will be like "Bearer aesrdtfcvghjbwu2:2wt62iydhu3odh"
            //so token will start from 7th index until last
            jwtToken=authHeader.substring(7); // remove "Bearer "
            //now we have to validate this token
            //first extract username out of this token
            String username=jwTservice.extractUsername(jwtToken);
            //securityContextHolder holds the current request user details
            //if some other filter already mentioned that this user is already authenticated for this request , then we dont have to re-verify
            //if the user is already authenticated then we don't have to do this action
            //check if the username from the token exists in the database
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                 //if no other filter stamped this request user is authenticated for this request
                //now if everything is ok,then we will mark this request as authenticated , then other filters can skip authentication verfiy
                //first check token expiration
                if(!jwTservice.expiredToken(jwtToken)){
                    //then get the username from the database with the userId
                    UserDetails curr_req_user=context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);//getting the bean instead of autowiring to avoid circular dependency
                    if(curr_req_user!=null ){//if and only if the user exists in the database , mark this user for this request as authenticated
                        //set the current request securityContextHolder as authenticated
                        //create auth object and the security to it
                        //usernamePasswordAuthenticationToken object have to has the logged in users or authenticated users info
                        //so to mark this user as authenticated for this request, we have to set this object , it has the user_details , i.e principal , credentials , authorities
                        UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(curr_req_user,null,curr_req_user.getAuthorities());
                        auth.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        //finally if everything is ok , set the authentication part , so that other filters can skip this task
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        }
        //else we will go with other filter
        filterChain.doFilter(request,response);
    }
}
