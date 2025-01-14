package com.mertncu.UniversityClubManagementFramework.controller;

import com.mertncu.UniversityClubManagementFramework.entity.Club;
import com.mertncu.UniversityClubManagementFramework.service.club.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class ClubController {

    @Autowired
    private ClubService clubService;

    // Tüm kulüpleri listeleme
    @GetMapping("/clubs/getAllClubs")
    public List<Club> getAllClubs() {
        return clubService.getAllClubs();
    }

    // Kulüp ekleme
    @PostMapping("/clubs/createClub")
    public ResponseEntity<Club> addClub(@RequestBody Club club) {
        Club createdClub = clubService.addClub(club);
        return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
    }

    // Kulüp güncelleme
    @PutMapping("/clubs/updateClub/{clubId}")
    public ResponseEntity<Club> updateClub(@PathVariable Long clubId, @RequestBody Club clubDetails) {
        Club updatedClub = clubService.updateClub(clubId, clubDetails);
        if (updatedClub != null) {
            return new ResponseEntity<>(updatedClub, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Kulüp bulunamadıysa 404 döner
    }

    // Kulüp silme
    @DeleteMapping("/clubs/deleteClub/{clubId}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long clubId) {
        clubService.deleteClub(clubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Silme başarılı, 204 döner
    }

    // Kulüp adıyla arama
    @GetMapping("/clubs/getClub/name")
    public ResponseEntity<Club> getClubByName(@RequestParam String name) {
        Club club = clubService.getClubByName(name);
        if (club != null) {
            return new ResponseEntity<>(club, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Bulunamadıysa 404 döner
    }
}
