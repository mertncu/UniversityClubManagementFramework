package com.mertncu.UniversityClubManagementFramework.controller.event;

import com.mertncu.UniversityClubManagementFramework.dto.auth.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.dto.event.EventParticipantDTO;
import com.mertncu.UniversityClubManagementFramework.entity.event.EventParticipant;
import com.mertncu.UniversityClubManagementFramework.service.event.EventParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public")
public class EventParticipantController {

    @Autowired
    private EventParticipantService eventParticipantService;

    @PostMapping("/event-participants/addParticipant")
    public ResponseEntity<AuthReqResDTO> addParticipantToEvent(
            @RequestBody EventParticipantDTO eventParticipantDTO) {

        EventParticipant participant = eventParticipantService.addParticipantToEvent(
                eventParticipantDTO.getEventId(),
                eventParticipantDTO.getParticipantName()
        );

        // Create an EventParticipantDTO from the saved EventParticipant
        EventParticipantDTO addedParticipantDTO = new EventParticipantDTO();
        addedParticipantDTO.setEventId(participant.getEventId());
        addedParticipantDTO.setParticipantName(participant.getParticipantName());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Participant added to event");
        response.setEventParticipantDTO(addedParticipantDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/event-participants/getParticipants/{eventId}")
    public ResponseEntity<AuthReqResDTO> getParticipantsByEvent(@PathVariable int eventId) {
        List<EventParticipant> participants = eventParticipantService.getParticipantsByEvent(eventId);

        // Map EventParticipant objects to EventParticipantDTOs
        List<EventParticipantDTO> participantDTOs = participants.stream()
                .map(p -> {
                    EventParticipantDTO dto = new EventParticipantDTO();
                    dto.setEventId(p.getEventId());
                    dto.setParticipantName(p.getParticipantName());
                    return dto;
                })
                .collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setEventParticipantDTOs(participantDTOs); // Set the DTO list
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/event-participants/removeParticipant")
    public ResponseEntity<AuthReqResDTO> removeParticipantFromEvent(
            @RequestParam int eventId,
            @RequestParam String participantName) {
        eventParticipantService.removeParticipantFromEvent(eventId, participantName);
        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.NO_CONTENT.value());
        response.setMessage("Participant removed from event");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}