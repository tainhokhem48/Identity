package com.example.SpringBoot.dto.request;

import com.example.SpringBoot.entity.Permission;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String name;
    private String description;
    Set<String> permissions;
}
