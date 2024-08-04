package com.example.SpringBoot.configuration;

import com.example.SpringBoot.entity.User;
import com.example.SpringBoot.enums.Role;
import com.example.SpringBoot.reponsitory.UserReponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner (UserReponsitory userReponsitory) {
        return args -> {
             if (userReponsitory.findByUsername("Admin").isEmpty()){
                 var role = new HashSet<String>();
                 role.add(Role.ADMIN.name());
                 User user = User.builder()
                         .username("admin")
                         .password(passwordEncoder.encode("admin"))
//                         .roles(role)
                         .build();
                 userReponsitory.save(user);
                 log.warn("admin user created password admin");
             }
        };
    }
}
