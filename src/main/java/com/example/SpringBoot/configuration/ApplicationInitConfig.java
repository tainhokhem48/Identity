package com.example.SpringBoot.configuration;

import com.example.SpringBoot.entity.User;
import com.example.SpringBoot.entity.Role;
import com.example.SpringBoot.reponsitory.RoleRepository;
import com.example.SpringBoot.reponsitory.UserReponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Bean
    ApplicationRunner applicationRunner (UserReponsitory userReponsitory) {
        return args -> {
            Role adminRole = roleRepository.findById("ADMIN").orElseGet(() -> {
                Role role = Role.builder()
                        .name("ADMIN")
                        .description("Administrator role")
                        .build();
                return roleRepository.save(role);
            });
             if (userReponsitory.findByUsername("Admin").isEmpty()){
                 Set<Role> roles = new HashSet<>();
                 roles.add(adminRole);
                 User user = User.builder()
                         .username("admin")
                         .password(passwordEncoder.encode("admin"))
                         .roles(roles)
                         .build();
                 userReponsitory.save(user);
                 log.warn("admin user created password admin");
             }
        };
    }
}
