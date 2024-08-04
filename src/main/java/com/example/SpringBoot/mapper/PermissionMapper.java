package com.example.SpringBoot.mapper;

import com.example.SpringBoot.dto.request.PermissionRequest;
import com.example.SpringBoot.dto.response.PermissionResponse;
import com.example.SpringBoot.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
