package com.example.SpringBoot.mapper;

import com.example.SpringBoot.dto.request.RoleRequest;
import com.example.SpringBoot.dto.response.RoleResponse;
import com.example.SpringBoot.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
