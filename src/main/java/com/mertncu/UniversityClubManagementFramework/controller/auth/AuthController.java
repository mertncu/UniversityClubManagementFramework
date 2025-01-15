package com.mertncu.UniversityClubManagementFramework.controller.auth;

import com.mertncu.UniversityClubManagementFramework.dto.auth.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.service.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserManagementService usersManagementService;

    @PostMapping("/register")
    public ResponseEntity<AuthReqResDTO> register(@RequestBody AuthReqResDTO registrationRequest) {
        return ResponseEntity.ok(usersManagementService.register(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthReqResDTO> login(@RequestBody AuthReqResDTO loginRequest) {
        return ResponseEntity.ok(usersManagementService.login(loginRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthReqResDTO> refreshToken(@RequestBody AuthReqResDTO refreshTokenRequest) {
        return ResponseEntity.ok(usersManagementService.refreshToken(refreshTokenRequest));
    }
}
