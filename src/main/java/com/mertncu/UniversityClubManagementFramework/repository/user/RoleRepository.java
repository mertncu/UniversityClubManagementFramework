package com.mertncu.UniversityClubManagementFramework.repository.user;

import com.mertncu.UniversityClubManagementFramework.entity.club.ClubRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<ClubRole, Integer> {
    Optional<ClubRole> findByRoleName(String roleName);
}
