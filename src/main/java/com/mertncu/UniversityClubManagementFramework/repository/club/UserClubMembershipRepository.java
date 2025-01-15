package com.mertncu.UniversityClubManagementFramework.repository.club;

import com.mertncu.UniversityClubManagementFramework.entity.club.UserClubMembership;
import com.mertncu.UniversityClubManagementFramework.entity.club.UserClubMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClubMembershipRepository extends JpaRepository<UserClubMembership, UserClubMembershipId> {
    List<UserClubMembership> findByClubId(Integer clubId);
    List<UserClubMembership> findByUserId(String userId);
}
