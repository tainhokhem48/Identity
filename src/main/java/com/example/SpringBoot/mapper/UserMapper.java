package com.example.SpringBoot.mapper;

import com.example.SpringBoot.dto.request.UserCreationRequest;
import com.example.SpringBoot.dto.request.UserUpdateRequest;
import com.example.SpringBoot.dto.response.UserResponse;
import com.example.SpringBoot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper  {
    User toUser(UserCreationRequest request);
    @Mapping(source = "id", target = "id")
    UserResponse toUserResponse( User user);
    @Mapping(target = "roles", ignore = true)
    void EditUser(@MappingTarget User user, UserUpdateRequest request);
}
