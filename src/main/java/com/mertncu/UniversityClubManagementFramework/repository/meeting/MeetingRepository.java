package com.mertncu.UniversityClubManagementFramework.repository.meeting;

import com.mertncu.UniversityClubManagementFramework.entity.meeting.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    List<Meeting> findByClubId(int clubId);
}