package com.mertncu.UniversityClubManagementFramework.service.club;

import com.mertncu.UniversityClubManagementFramework.entity.Club;
import com.mertncu.UniversityClubManagementFramework.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club addClub(Club club) {
        return clubRepository.save(club);
    }

    public Club updateClub(Long clubId, Club clubDetails) {
        Optional<Club> optionalClub = clubRepository.findById(clubId);
        if (optionalClub.isPresent()) {
            Club existingClub = optionalClub.get();
            existingClub.setName(clubDetails.getName());
            existingClub.setDescription(clubDetails.getDescription());
            existingClub.setClubPicture(clubDetails.getClubPicture());
            return clubRepository.save(existingClub);
        }
        return null;
    }

    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    public Club getClubByName(String name) {
        return clubRepository.findByName(name);
    }
}
