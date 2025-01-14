package com.mertncu.UniversityClubManagementFramework.service;

import com.mertncu.UniversityClubManagementFramework.entity.UserClubMembership;
import com.mertncu.UniversityClubManagementFramework.entity.UserClubMembershipId;
import com.mertncu.UniversityClubManagementFramework.repository.UserClubMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserClubMembershipService {
    @Autowired
    private UserClubMembershipRepository repository;

    public UserClubMembership assignUserToClub(String userId, Integer clubId) {
        UserClubMembership membership = new UserClubMembership(userId, clubId);
        return repository.save(membership);
    }

    public void removeUserFromClub(String userId, Integer clubId) {
        UserClubMembershipId membershipId = new UserClubMembershipId(userId, clubId);
        repository.deleteById(membershipId);
    }

    public List<UserClubMembership> getUsersByClub(Integer clubId) {
        return repository.findByClubId(clubId);
    }

    public List<UserClubMembership> getClubsByUser(String userId) {
        return repository.findByUserId(userId);
    }
}
