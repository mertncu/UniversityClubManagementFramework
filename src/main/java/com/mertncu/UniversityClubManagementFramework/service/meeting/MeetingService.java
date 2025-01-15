package com.mertncu.UniversityClubManagementFramework.service.meeting;

import com.mertncu.UniversityClubManagementFramework.entity.meeting.Meeting;
import com.mertncu.UniversityClubManagementFramework.exception.ResourceNotFoundException;
import com.mertncu.UniversityClubManagementFramework.repository.meeting.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public Meeting saveMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public Meeting getMeetingById(int meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting", "id", meetingId));
    }

    public List<Meeting> getMeetingsByClubId(int clubId) {
        return meetingRepository.findByClubId(clubId);
    }

    public Meeting updateMeeting(int meetingId, Meeting meetingDetails) {
        Meeting meeting = getMeetingById(meetingId); // Reuse for exception handling
        meeting.setMeetingName(meetingDetails.getMeetingName());
        meeting.setMeetingDate(meetingDetails.getMeetingDate());
        meeting.setMeetingLocation(meetingDetails.getMeetingLocation());
        meeting.setDescription(meetingDetails.getDescription());
        meeting.setClubId(meetingDetails.getClubId());
        return meetingRepository.save(meeting);
    }

    public void deleteMeeting(int meetingId) {
        Meeting meeting = getMeetingById(meetingId); // Check existence
        meetingRepository.delete(meeting);
    }
}