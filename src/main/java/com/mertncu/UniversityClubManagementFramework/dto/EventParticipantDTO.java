package com.mertncu.UniversityClubManagementFramework.dto;

import lombok.Data;

@Data
public class EventParticipantDTO {
    private int eventId;
    private String participantName;

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