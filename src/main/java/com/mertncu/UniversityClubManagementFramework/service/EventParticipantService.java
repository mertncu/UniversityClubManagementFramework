package com.mertncu.UniversityClubManagementFramework.service;

import com.mertncu.UniversityClubManagementFramework.entity.EventParticipant;
import com.mertncu.UniversityClubManagementFramework.entity.EventParticipantId;
import com.mertncu.UniversityClubManagementFramework.exception.ResourceNotFoundException;
import com.mertncu.UniversityClubManagementFramework.repository.EventParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventParticipantService {

    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    public EventParticipant addParticipantToEvent(int eventId, String participantName) {
        EventParticipant participant = new EventParticipant();
        participant.setEventId(eventId);
        participant.setParticipantName(participantName);
        return eventParticipantRepository.save(participant);
    }

    public List<EventParticipant> getParticipantsByEvent(int eventId) {
        return eventParticipantRepository.findByEventId(eventId);
    }

    public void removeParticipantFromEvent(int eventId, String participantName) {
        EventParticipantId id = new EventParticipantId(eventId, participantName);
        if (!eventParticipantRepository.existsById(id)) {
            throw new ResourceNotFoundException("EventParticipant", "eventId & participantName", eventId + " & " + participantName);
        }
        eventParticipantRepository.deleteById(id);
    }
}