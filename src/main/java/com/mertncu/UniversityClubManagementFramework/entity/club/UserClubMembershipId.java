package com.mertncu.UniversityClubManagementFramework.entity.club;

import java.io.Serializable;
import java.util.Objects;

public class UserClubMembershipId implements Serializable {
    private String userId;
    private Integer clubId;

    public UserClubMembershipId(String userId, Integer clubId) {
        this.userId = userId;
        this.clubId = clubId;
    }

    public UserClubMembershipId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClubMembershipId that = (UserClubMembershipId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(clubId, that.clubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clubId);
    }
}
