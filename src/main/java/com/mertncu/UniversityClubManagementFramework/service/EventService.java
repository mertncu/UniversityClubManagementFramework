package com.mertncu.UniversityClubManagementFramework.service;

import com.mertncu.UniversityClubManagementFramework.entity.Event;
import com.mertncu.UniversityClubManagementFramework.exception.ResourceNotFoundException;
import com.mertncu.UniversityClubManagementFramework.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
    }

    public List<Event> getEventsByClubId(int clubId) {
        return eventRepository.findByClubId(clubId);
    }

    public Event updateEvent(int eventId, Event eventDetails) {
        Event event = getEventById(eventId); // Reuse getEventById for exception handling
        event.setEventName(eventDetails.getEventName());
        event.setEventDate(eventDetails.getEventDate());
        event.setEventLocation(eventDetails.getEventLocation());
        event.setDescription(eventDetails.getDescription());
        event.setClubId(eventDetails.getClubId());
        return eventRepository.save(event);
    }

    public void deleteEvent(int eventId) {
        Event event = getEventById(eventId); // Check if exists before deleting
        eventRepository.delete(event);
    }
}