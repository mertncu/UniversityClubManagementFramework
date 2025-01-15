package com.mertncu.UniversityClubManagementFramework.repository.event;

import com.mertncu.UniversityClubManagementFramework.entity.event.EventParticipant;
import com.mertncu.UniversityClubManagementFramework.entity.event.EventParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, EventParticipantId> {
    List<EventParticipant> findByEventId(int eventId);
}