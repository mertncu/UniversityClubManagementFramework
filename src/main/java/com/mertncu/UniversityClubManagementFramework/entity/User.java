package com.mertncu.UniversityClubManagementFramework.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity
@Data
public class User implements UserDetails {

    @Id
    private String id;

    private String name;
    private String password;
    private String email;
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_club_id", referencedColumnName = "club_id", foreignKey = @ForeignKey(name = "fk_primary_club"))
    private Club primaryClub;  // Relation to the Club entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_role_id", referencedColumnName = "role_id", foreignKey = @ForeignKey(name = "fk_primary_role"))
    private ClubRole primaryRole;

    @Version
    private Integer version;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = Role.valueOf(role.toUpperCase()).name();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Club getPrimaryClub() {
        return primaryClub;
    }

    public void setPrimaryClub(Club primaryClub) {
        this.primaryClub = primaryClub;
    }

    public ClubRole getPrimaryRole() {
        return primaryRole;
    }

    public void setPrimaryRole(ClubRole primaryRole) {
        this.primaryRole = primaryRole;
    }
}