package com.example.SpringBoot.controllers;

import com.example.SpringBoot.UserService.UserService;
import com.example.SpringBoot.dto.request.ApiResponse;
import com.example.SpringBoot.dto.request.UserCreationRequest;
import com.example.SpringBoot.dto.request.UserUpdateRequest;
import com.example.SpringBoot.dto.response.UserResponse;
import com.example.SpringBoot.entity.User;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> CreateUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.CreateUser(request));
        return apiResponse;
    }
    @GetMapping
    ApiResponse<List<UserResponse>> GetUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .code(1000)
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserId(@PathVariable("userId") String userId){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .code(1000)
                .build();
    }

    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .code(1000)
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<User> EditUser(@Valid @PathVariable("userId") String userId, @RequestBody UserUpdateRequest request){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.EditUser(userId,request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
     String deleteUser(@PathVariable("userId") String userId){
          userService.deleteUser(userId);
          return "delete user Successfully";
    }
}
