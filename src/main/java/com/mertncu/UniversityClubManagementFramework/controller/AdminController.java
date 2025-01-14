package com.mertncu.UniversityClubManagementFramework.controller;

import com.mertncu.UniversityClubManagementFramework.dto.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.entity.User;
import com.mertncu.UniversityClubManagementFramework.service.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserManagementService usersManagementService;

    @PostMapping("/create-user")
    public ResponseEntity<AuthReqResDTO> createUser(@RequestBody User user) {
        return ResponseEntity.ok(usersManagementService.createUser(user));
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<AuthReqResDTO> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @GetMapping("/get-users/{userId}")
    public ResponseEntity<AuthReqResDTO> getUserById(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.getUserById(userId));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<AuthReqResDTO> updateUser(@PathVariable Integer userId, @RequestBody User updateUserRequest){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, updateUserRequest));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<AuthReqResDTO> deleteUser(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }
}
