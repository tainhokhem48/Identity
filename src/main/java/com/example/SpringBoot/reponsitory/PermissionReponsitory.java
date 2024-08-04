package com.example.SpringBoot.reponsitory;

import com.example.SpringBoot.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionReponsitory extends JpaRepository<Permission, String> {
}
