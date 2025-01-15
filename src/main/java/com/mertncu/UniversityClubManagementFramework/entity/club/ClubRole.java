package com.mertncu.UniversityClubManagementFramework.entity.club;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "Roles")
@Entity
@Data
public class ClubRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    // Getter ve Setter'lar
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
