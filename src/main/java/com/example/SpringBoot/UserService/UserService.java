package com.example.SpringBoot.UserService;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.SpringBoot.dto.request.UserCreationRequest;
import com.example.SpringBoot.dto.request.UserUpdateRequest;
import com.example.SpringBoot.dto.response.UserResponse;
import com.example.SpringBoot.entity.User;
import com.example.SpringBoot.enums.Role;
import com.example.SpringBoot.exception.ErrorCode;
import com.example.SpringBoot.exception.ErrorCodeException;
import com.example.SpringBoot.mapper.UserMapper;
import com.example.SpringBoot.reponsitory.RoleRepository;
import com.example.SpringBoot.reponsitory.UserReponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserReponsitory userReponsitory;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserResponse CreateUser(UserCreationRequest request){

        if (userReponsitory.existsByUsername(request.getUsername())){
            throw new ErrorCodeException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);
        return userMapper.toUserResponse(userReponsitory.save(user));
    }
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<UserResponse> getUsers(){
        log.info("in method user");
        return userReponsitory.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userReponsitory.findById(id).orElseThrow(()-> new ErrorCodeException(ErrorCode.ID_NOTFOUND)));
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userReponsitory.findByUsername(name)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public UserResponse EditUser(String id, UserUpdateRequest request){
        User user = userReponsitory.findById(id).orElseThrow(() -> new ErrorCodeException(ErrorCode.ID_NOTFOUND));
        userMapper.EditUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        User updateUser = userReponsitory.save(user);
        return userMapper.toUserResponse(updateUser);
    }

    public void deleteUser(String id){
        userReponsitory.deleteById(id);
    }
}
