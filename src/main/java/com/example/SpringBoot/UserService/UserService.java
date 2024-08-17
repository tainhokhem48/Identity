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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        com.example.SpringBoot.entity.Role userRole = roleRepository.findById(Role.USER.name())
                .orElseGet(()->{
                    com.example.SpringBoot.entity.Role newRole = new com.example.SpringBoot.entity.Role();
                    newRole.setName(Role.USER.name());
                    return roleRepository.save(newRole);
                });

        HashSet<com.example.SpringBoot.entity.Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        try {
            user = userReponsitory.save(user);
        }catch(DataIntegrityViolationException exception){
            throw new ErrorCodeException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
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
