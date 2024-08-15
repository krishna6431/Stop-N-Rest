package com.stopnrest.auth.repository;
import com.stopnrest.auth.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUserName(String username);

    boolean existsByUserName(String username);
}
