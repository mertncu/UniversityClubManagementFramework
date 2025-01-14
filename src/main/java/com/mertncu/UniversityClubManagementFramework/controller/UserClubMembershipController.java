package com.mertncu.UniversityClubManagementFramework.controller;

import com.mertncu.UniversityClubManagementFramework.entity.UserClubMembership;
import com.mertncu.UniversityClubManagementFramework.service.UserClubMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class UserClubMembershipController {
    @Autowired
    private UserClubMembershipService service;

    @PostMapping("/club/member/assign")
    public ResponseEntity<String> assignUserToClub(@RequestParam String userId, @RequestParam Integer clubId) {
        service.assignUserToClub(userId, clubId);
        return ResponseEntity.ok("User assigned to club successfully.");
    }

    @DeleteMapping("/club/member/remove")
    public ResponseEntity<String> removeUserFromClub(@RequestParam String userId, @RequestParam Integer clubId) {
        service.removeUserFromClub(userId, clubId);
        return ResponseEntity.ok("User removed from club successfully.");
    }

    @GetMapping("/users/{clubId}")
    public ResponseEntity<List<UserClubMembership>> getUsersByClub(@PathVariable Integer clubId) {
        return ResponseEntity.ok(service.getUsersByClub(clubId));
    }

    @GetMapping("/clubs/{userId}")
    public ResponseEntity<List<UserClubMembership>> getClubsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getClubsByUser(userId));
    }
}

