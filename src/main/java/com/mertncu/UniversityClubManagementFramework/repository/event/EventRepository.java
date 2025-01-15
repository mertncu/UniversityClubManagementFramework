package com.mertncu.UniversityClubManagementFramework.repository.event;

import com.mertncu.UniversityClubManagementFramework.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByClubId(int clubId);
}