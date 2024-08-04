package com.example.SpringBoot.dto.request;

import com.example.SpringBoot.validatior.DobConstraint;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserUpdateRequest {
    private String password;
    private String firstName;
    private String lastName;
    @DobConstraint(min = 18, message = "UN_VALID_DOB")
    private LocalDate dob;
    List<String> roles;
}
