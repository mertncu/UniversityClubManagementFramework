package com.mertncu.UniversityClubManagementFramework.repository.user;

import com.mertncu.UniversityClubManagementFramework.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String > {
    Optional<User> findByEmail(String email);
}
