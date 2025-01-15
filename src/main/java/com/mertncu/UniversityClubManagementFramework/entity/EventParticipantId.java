package com.mertncu.UniversityClubManagementFramework.entity;

import java.io.Serializable;
import java.util.Objects;

public class EventParticipantId implements Serializable {
    private int eventId;
    private String participantName;

    public EventParticipantId() {}

    public EventParticipantId(int eventId, String participantName) {
        this.eventId = eventId;
        this.participantName = participantName;
    }

    // Getters and Setters

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
}