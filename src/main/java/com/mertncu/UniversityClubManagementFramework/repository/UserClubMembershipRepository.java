package com.mertncu.UniversityClubManagementFramework.repository;

import com.mertncu.UniversityClubManagementFramework.entity.UserClubMembership;
import com.mertncu.UniversityClubManagementFramework.entity.UserClubMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClubMembershipRepository extends JpaRepository<UserClubMembership, UserClubMembershipId> {
    List<UserClubMembership> findByClubId(Integer clubId);
    List<UserClubMembership> findByUserId(String userId);
}
