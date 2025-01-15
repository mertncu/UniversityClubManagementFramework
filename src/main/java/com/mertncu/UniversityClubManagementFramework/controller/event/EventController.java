package com.mertncu.UniversityClubManagementFramework.controller.event;

import com.mertncu.UniversityClubManagementFramework.dto.auth.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.dto.event.EventDTO;
import com.mertncu.UniversityClubManagementFramework.entity.club.Club;
import com.mertncu.UniversityClubManagementFramework.entity.event.Event;
import com.mertncu.UniversityClubManagementFramework.service.event.EventService;
import com.mertncu.UniversityClubManagementFramework.service.club.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/public")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ClubService clubService;

    @PostMapping("/events/createEvent")
    public ResponseEntity<AuthReqResDTO> createEvent(@RequestBody Event event) {
        Event newEvent = eventService.saveEvent(event);
        Club club = clubService.getClubById(newEvent.getClubId());

        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventId(newEvent.getEventId());
        eventDTO.setClubId(newEvent.getClubId());
        eventDTO.setEventName(newEvent.getEventName());
        eventDTO.setEventDate(newEvent.getEventDate());
        eventDTO.setEventLocation(newEvent.getEventLocation());
        eventDTO.setDescription(newEvent.getDescription());
        eventDTO.setClubName(club.getName());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Event created successfully");
        response.setEventDTO(eventDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/events/getAllEvents")
    public ResponseEntity<AuthReqResDTO> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setEvents(events);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/events/getEvent/{eventId}")
    public ResponseEntity<AuthReqResDTO> getEventById(@PathVariable int eventId) {
        Event event = eventService.getEventById(eventId);
        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setEvent(event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/events/getEventsByClubId/{clubId}")
    public ResponseEntity<AuthReqResDTO> getEventsByClubId(@PathVariable int clubId) {
        List<Event> events = eventService.getEventsByClubId(clubId);
        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setEvents(events);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/events/updateEvent/{eventId}")
    public ResponseEntity<AuthReqResDTO> updateEvent(@PathVariable int eventId, @RequestBody Event eventDetails) {
        Event updatedEvent = eventService.updateEvent(eventId, eventDetails);
        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Event updated successfully");
        response.setEvent(updatedEvent);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/events/deleteEvent/{eventId}")
    public ResponseEntity<AuthReqResDTO> deleteEvent(@PathVariable int eventId) {
        eventService.deleteEvent(eventId);
        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.NO_CONTENT.value());
        response.setMessage("Event deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}