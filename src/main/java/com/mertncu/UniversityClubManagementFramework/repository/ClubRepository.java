package com.mertncu.UniversityClubManagementFramework.repository;

import com.mertncu.UniversityClubManagementFramework.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByName(String name);
}
