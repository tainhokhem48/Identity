package com.example.SpringBoot.controllers;

import com.example.SpringBoot.UserService.PermissionService;
import com.example.SpringBoot.dto.request.ApiResponse;
import com.example.SpringBoot.dto.request.PermissionRequest;
import com.example.SpringBoot.dto.response.PermissionResponse;
import com.example.SpringBoot.entity.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Slf4j
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission ){

        permissionService.delete(permission);
        return ApiResponse.<Void>builder().build();
    }
}
