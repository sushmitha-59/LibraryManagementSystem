package com.example.minor_project.model;

import com.example.minor_project.Utilities.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Users implements UserDetails , Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private String authorities;

    //for checking which user got logged in
    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Student student;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Student admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        if(this.authorities ==null){
            try {
                throw new Exception("Authorities are null");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("printing autheritites in User mode "+ this.authorities);
        return Arrays.stream(this.authorities.split(Constants.DELIMITER)) //authorities will be like "CREATE_BOOK::CREATE_ADMIN::CREATE_AUTHOR"
                .map(auth->new SimpleGrantedAuthority(auth)) //for every String authority we are making object and storing it in the Authority_list for that user
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Users {"+
                "id :"+this.id+"\"" +
                " ,username : "+this.username+"\"" +
                " ,authorities : "+this.authorities+"\"" +
                "}";
    }
}
