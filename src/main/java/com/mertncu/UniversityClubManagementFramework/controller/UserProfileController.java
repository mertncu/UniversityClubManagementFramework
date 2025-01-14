package com.mertncu.UniversityClubManagementFramework.controller;

import com.mertncu.UniversityClubManagementFramework.dto.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminuser")
public class UserProfileController {
    @Autowired
    private UserManagementService usersManagementService;

    @GetMapping("/get-profile")
    public ResponseEntity<AuthReqResDTO> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AuthReqResDTO response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
