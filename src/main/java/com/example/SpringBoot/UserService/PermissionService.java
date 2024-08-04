package com.example.SpringBoot.UserService;

import com.example.SpringBoot.dto.request.PermissionRequest;
import com.example.SpringBoot.dto.response.PermissionResponse;
import com.example.SpringBoot.entity.Permission;
import com.example.SpringBoot.mapper.PermissionMapper;
import com.example.SpringBoot.reponsitory.PermissionReponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PermissionService {
    @Autowired
    private PermissionReponsitory permissionReponsitory;
    @Autowired
    private PermissionMapper permissionMapper;
    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionReponsitory.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionReponsitory.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionReponsitory.deleteById(permission);
    }
}
