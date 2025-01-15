package com.mertncu.UniversityClubManagementFramework.controller.user;

import com.mertncu.UniversityClubManagementFramework.entity.club.ClubRole;
import com.mertncu.UniversityClubManagementFramework.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles/getAllRoles")
    public List<ClubRole> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/roles/createRole")
    public ResponseEntity<ClubRole> createRole(@RequestBody ClubRole role) {
        ClubRole createdRole = roleService.addRole(role);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/roles/updateRole/{roleId}")
    public ResponseEntity<ClubRole> updateRole(@PathVariable int roleId, @RequestBody ClubRole roleDetails) {
        ClubRole updatedRole = roleService.updateRole(roleId, roleDetails);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @DeleteMapping("/roles/deleteRole/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable int roleId) {
        roleService.deleteRole(roleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
