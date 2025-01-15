package com.mertncu.UniversityClubManagementFramework.entity.club;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@IdClass(UserClubMembershipId.class)
@Data
@Table(name = "user_club_memberships")
public class UserClubMembership {
    @Id
    private String userId;

    @Id
    private Integer clubId;
    public UserClubMembership(String userId, Integer clubId) {
        this.userId = userId;
        this.clubId = clubId;
    }

    public UserClubMembership() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    @Override
    public String toString() {
        return "UserClubMembership{" +
                "userId='" + userId + '\'' +
                ", clubId=" + clubId +
                '}';
    }
}
