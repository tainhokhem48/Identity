package com.example.SpringBoot.UserService;

import com.example.SpringBoot.dto.request.RoleRequest;
import com.example.SpringBoot.dto.response.RoleResponse;
import com.example.SpringBoot.entity.Role;
import com.example.SpringBoot.mapper.RoleMapper;
import com.example.SpringBoot.reponsitory.PermissionReponsitory;
import com.example.SpringBoot.reponsitory.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionReponsitory permissionReponsitory;

    public RoleResponse creater(RoleRequest request){
        var role = roleMapper.toRole(request);
        var per = permissionReponsitory.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(per));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getRole(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

}
