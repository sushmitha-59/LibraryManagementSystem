package com.example.minor_project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class AdminResponse {
    private Integer id;
    private String name;
    private String email;
    private Date CreatedOn;
    private Date UpdatedOn;
}
