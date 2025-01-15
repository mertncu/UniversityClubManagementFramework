package com.mertncu.UniversityClubManagementFramework.controller.meeting;

import com.mertncu.UniversityClubManagementFramework.dto.auth.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.dto.meeting.MeetingDTO;
import com.mertncu.UniversityClubManagementFramework.entity.meeting.Meeting;
import com.mertncu.UniversityClubManagementFramework.service.club.ClubService;
import com.mertncu.UniversityClubManagementFramework.service.meeting.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ClubService clubService; // To fetch club name for DTO

    @PostMapping("/meetings/createMeeting")
    public ResponseEntity<AuthReqResDTO> createMeeting(@RequestBody MeetingDTO meetingDTO) {
        Meeting meeting = new Meeting();
        meeting.setClubId(meetingDTO.getClubId());
        meeting.setMeetingName(meetingDTO.getMeetingName());
        meeting.setMeetingDate(meetingDTO.getMeetingDate());
        meeting.setMeetingLocation(meetingDTO.getMeetingLocation());
        meeting.setDescription(meetingDTO.getDescription());

        Meeting newMeeting = meetingService.saveMeeting(meeting);

        // Create MeetingDTO from the saved Meeting
        MeetingDTO createdMeetingDTO = new MeetingDTO();
        createdMeetingDTO.setMeetingId(newMeeting.getMeetingId());
        createdMeetingDTO.setClubId(newMeeting.getClubId());
        createdMeetingDTO.setMeetingName(newMeeting.getMeetingName());
        createdMeetingDTO.setMeetingDate(newMeeting.getMeetingDate());
        createdMeetingDTO.setMeetingLocation(newMeeting.getMeetingLocation());
        createdMeetingDTO.setDescription(newMeeting.getDescription());

        // Fetch and set the club name
        var club = clubService.getClubById(newMeeting.getClubId());
        if (club != null) {
            createdMeetingDTO.setClubName(club.getName());
        }

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Meeting created successfully");
        response.setMeetingDTO(createdMeetingDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/meetings/getAllMeetings")
    public ResponseEntity<AuthReqResDTO> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();

        // Map Meeting objects to MeetingDTOs
        List<MeetingDTO> meetingDTOs = meetings.stream()
                .map(meeting -> {
                    MeetingDTO dto = new MeetingDTO();
                    dto.setMeetingId(meeting.getMeetingId());
                    dto.setClubId(meeting.getClubId());
                    dto.setMeetingName(meeting.getMeetingName());
                    dto.setMeetingDate(meeting.getMeetingDate());
                    dto.setMeetingLocation(meeting.getMeetingLocation());
                    dto.setDescription(meeting.getDescription());

                    // Fetch and set the club name
                    var club = clubService.getClubById(meeting.getClubId());
                    if (club != null) {
                        dto.setClubName(club.getName());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMeetingDTOs(meetingDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/meetings/getMeeting/{meetingId}")
    public ResponseEntity<AuthReqResDTO> getMeetingById(@PathVariable int meetingId) {
        Meeting meeting = meetingService.getMeetingById(meetingId);

        // Create MeetingDTO
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setMeetingId(meeting.getMeetingId());
        meetingDTO.setClubId(meeting.getClubId());
        meetingDTO.setMeetingName(meeting.getMeetingName());
        meetingDTO.setMeetingDate(meeting.getMeetingDate());
        meetingDTO.setMeetingLocation(meeting.getMeetingLocation());
        meetingDTO.setDescription(meeting.getDescription());

        // Fetch and set the club name
        var club = clubService.getClubById(meeting.getClubId());
        if (club != null) {
            meetingDTO.setClubName(club.getName());
        }

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMeetingDTO(meetingDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/meetings/getMeetingsByClubId/{clubId}")
    public ResponseEntity<AuthReqResDTO> getMeetingsByClubId(@PathVariable int clubId) {
        List<Meeting> meetings = meetingService.getMeetingsByClubId(clubId);

        // Map to MeetingDTOs
        List<MeetingDTO> meetingDTOs = meetings.stream()
                .map(meeting -> {
                    MeetingDTO dto = new MeetingDTO();
                    dto.setMeetingId(meeting.getMeetingId());
                    dto.setClubId(meeting.getClubId());
                    dto.setMeetingName(meeting.getMeetingName());
                    dto.setMeetingDate(meeting.getMeetingDate());
                    dto.setMeetingLocation(meeting.getMeetingLocation());
                    dto.setDescription(meeting.getDescription());

                    // Fetch and set the club name
                    var club = clubService.getClubById(meeting.getClubId());
                    if (club != null) {
                        dto.setClubName(club.getName());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMeetingDTOs(meetingDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/meetings/updateMeeting/{meetingId}")
    public ResponseEntity<AuthReqResDTO> updateMeeting(@PathVariable int meetingId, @RequestBody MeetingDTO meetingDTO) {
        Meeting existingMeeting = meetingService.getMeetingById(meetingId);

        // Update the existing meeting with data from DTO
        existingMeeting.setClubId(meetingDTO.getClubId());
        existingMeeting.setMeetingName(meetingDTO.getMeetingName());
        existingMeeting.setMeetingDate(meetingDTO.getMeetingDate());
        existingMeeting.setMeetingLocation(meetingDTO.getMeetingLocation());
        existingMeeting.setDescription(meetingDTO.getDescription());

        Meeting updatedMeeting = meetingService.updateMeeting(meetingId, existingMeeting);

        // Create a MeetingDTO from the updated Meeting
        MeetingDTO updatedMeetingDTO = new MeetingDTO();
        updatedMeetingDTO.setMeetingId(updatedMeeting.getMeetingId());
        updatedMeetingDTO.setClubId(updatedMeeting.getClubId());
        updatedMeetingDTO.setMeetingName(updatedMeeting.getMeetingName());
        updatedMeetingDTO.setMeetingDate(updatedMeeting.getMeetingDate());
        updatedMeetingDTO.setMeetingLocation(updatedMeeting.getMeetingLocation());
        updatedMeetingDTO.setDescription(updatedMeeting.getDescription());

        // Fetch and set the club name
        var club = clubService.getClubById(updatedMeeting.getClubId());
        if (club != null) {
            updatedMeetingDTO.setClubName(club.getName());
        }

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Meeting updated successfully");
        response.setMeetingDTO(updatedMeetingDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/meetings/deleteMeeting/{meetingId}")
    public ResponseEntity<AuthReqResDTO> deleteMeeting(@PathVariable int meetingId) {
        meetingService.deleteMeeting(meetingId);

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.NO_CONTENT.value());
        response.setMessage("Meeting deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}