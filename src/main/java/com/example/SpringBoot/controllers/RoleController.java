package com.example.SpringBoot.controllers;

import com.example.SpringBoot.UserService.RoleService;
import com.example.SpringBoot.dto.request.ApiResponse;
import com.example.SpringBoot.dto.request.RoleRequest;
import com.example.SpringBoot.dto.response.RoleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> creatRole(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.creater(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getRole(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getRole())
                .build();
    }
}
