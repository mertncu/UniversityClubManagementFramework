package com.mertncu.UniversityClubManagementFramework.entity.event;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "event_participants")
@IdClass(EventParticipantId.class)
@Data
public class EventParticipant {

    @Id
    @Column(name = "event_id")
    private int eventId;

    @Id
    @Column(name = "participant_name")
    private String participantName;

    // Optional: Relationship with Event (ManyToOne) if you need it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // toString
    @Override
    public String toString() {
        return "EventParticipant{" +
                "eventId=" + eventId +
                ", participantName='" + participantName + '\'' +
                '}';
    }
}