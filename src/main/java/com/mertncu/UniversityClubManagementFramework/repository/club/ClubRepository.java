package com.mertncu.UniversityClubManagementFramework.repository.club;

import com.mertncu.UniversityClubManagementFramework.entity.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByName(String name);
}
