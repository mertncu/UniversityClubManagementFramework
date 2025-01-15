package com.mertncu.UniversityClubManagementFramework.service.role;

import com.mertncu.UniversityClubManagementFramework.entity.club.ClubRole;
import com.mertncu.UniversityClubManagementFramework.repository.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<ClubRole> getAllRoles() {
        return roleRepository.findAll();
    }

    public ClubRole addRole(ClubRole role) {
        return roleRepository.save(role);
    }

    public ClubRole updateRole(int roleId, ClubRole roleDetails) {
        ClubRole role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setRoleName(roleDetails.getRoleName());
        return roleRepository.save(role);
    }

    public void deleteRole(int roleId) {
        roleRepository.deleteById(roleId);
    }
}
