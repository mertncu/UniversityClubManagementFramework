package com.mertncu.UniversityClubManagementFramework.controller;

import com.mertncu.UniversityClubManagementFramework.dto.RequestResponseDTO;
import com.mertncu.UniversityClubManagementFramework.entity.User;
import com.mertncu.UniversityClubManagementFramework.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserManagementService usersManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<RequestResponseDTO> regeister(@RequestBody RequestResponseDTO registrationRequest) {
        return ResponseEntity.ok(usersManagementService.register(registrationRequest));
    }

    @PostMapping("/admin/create-user")
    public ResponseEntity<RequestResponseDTO> createUser(@RequestBody User user) {
        return ResponseEntity.ok(usersManagementService.createUser(user));
    }

        @PostMapping("/auth/login")
        public ResponseEntity<RequestResponseDTO> login(@RequestBody RequestResponseDTO loginRequest) {
            return ResponseEntity.ok(usersManagementService.login(loginRequest));
        }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<RequestResponseDTO> refreshToken(@RequestBody RequestResponseDTO refreshTokenRequest) {
        return ResponseEntity.ok(usersManagementService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<RequestResponseDTO> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<RequestResponseDTO> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.getUserById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<RequestResponseDTO> updateUser(@PathVariable Integer userId, @RequestBody User updateUserRequest){
        return ResponseEntity.ok(usersManagementService.updateUser(userId, updateUserRequest));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<RequestResponseDTO> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        RequestResponseDTO response = usersManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<RequestResponseDTO> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }
}