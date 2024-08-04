package com.example.SpringBoot.reponsitory;

import com.example.SpringBoot.entity.InValidateToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedRepository extends JpaRepository<InValidateToken, String> {
}
