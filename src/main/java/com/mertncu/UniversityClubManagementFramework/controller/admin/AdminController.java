package com.mertncu.UniversityClubManagementFramework.controller.admin;

import com.mertncu.UniversityClubManagementFramework.dto.auth.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.entity.user.User;
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
        AuthReqResDTO response = usersManagementService.createUser(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<AuthReqResDTO> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @GetMapping("/get-users/{userId}")
    public ResponseEntity<AuthReqResDTO> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(usersManagementService.getUserById(userId));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<AuthReqResDTO> updateUser(@PathVariable String userId, @RequestBody User updateUserRequest){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, updateUserRequest));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<AuthReqResDTO> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }
}
