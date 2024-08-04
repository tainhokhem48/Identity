package com.example.SpringBoot.dto.request;

import com.example.SpringBoot.validatior.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class UserCreationRequest {
    private String username;
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;
    private String firstName;
    private String lastName;

    @DobConstraint(min = 18, message = "UN_VALID_DOB")
    private LocalDate dob;
}
