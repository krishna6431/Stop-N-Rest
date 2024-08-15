package com.stopnrest.auth.repository;

import com.stopnrest.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
    boolean existsByUserName(String username);
}
