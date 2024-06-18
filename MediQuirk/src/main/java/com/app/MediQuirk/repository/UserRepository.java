package com.app.MediQuirk.repository;

import com.app.MediQuirk.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}