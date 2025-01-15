package com.mertncu.UniversityClubManagementFramework.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mertncu.UniversityClubManagementFramework.entity.Club;
import com.mertncu.UniversityClubManagementFramework.entity.Event;
import com.mertncu.UniversityClubManagementFramework.entity.User;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthReqResDTO {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String accessToken;
    private String expirationTime;
    private String id;
    private String name;
    private String email;
    private String city;
    private String role;
    private String password;
    private User user;
    private List<User> users;
    private List<Club> clubs;
    private Event event;
    private List<Event> events;
    private EventDTO eventDTO;
    private EventParticipantDTO eventParticipantDTO;
    private List<EventParticipantDTO> eventParticipantsDTOs;
    private MeetingDTO meetingDTO;
    private List<MeetingDTO> meetingDTOs;
    private TaskDTO taskDTO;
    private List<TaskDTO> taskDTOs;
    private FileDTO fileDTO;
    private List<FileDTO> fileDTOs;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // Getter and setter for 'event'
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // Getter and setter for 'events'
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    // Getter and setter for 'clubs'
    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

    public EventDTO getEventDTO() {
        return eventDTO;
    }

    public void setEventDTO(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
    }

    public EventParticipantDTO getEventParticipantDTO() {
        return eventParticipantDTO;
    }

    public void setEventParticipantDTO(EventParticipantDTO eventParticipantDTO) {
        this.eventParticipantDTO = eventParticipantDTO;
    }

    public List<EventParticipantDTO> getEventParticipantsDTOs() {
        return eventParticipantsDTOs;
    }

    public void setEventParticipantDTOs(List<EventParticipantDTO> eventParticipantsDTOs) {
        this.eventParticipantsDTOs = eventParticipantsDTOs;
    }

    public void setMeetingDTO(MeetingDTO meetingDTO) {
        this.meetingDTO = meetingDTO;
    }
    public void setMeetingDTOs(List<MeetingDTO> meetingDTOs) {
        this.meetingDTOs = meetingDTOs;
    }

    public MeetingDTO getMeetingDTO() {
        return meetingDTO;
    }

    // Getters and Setters for meetingDTOs
    public List<MeetingDTO> getMeetingDTOs() {
        return meetingDTOs;
    }

    public void setTaskDTO(TaskDTO taskDTO) {
        this.taskDTO = taskDTO;
    }

    public void setTaskDTOs(List<TaskDTO> taskDTOs) {
        this.taskDTOs = taskDTOs;
    }

    public TaskDTO getTaskDTO() {
        return taskDTO;
    }

    public List<TaskDTO> getTaskDTOs() {
        return taskDTOs;
    }

    public FileDTO getFileDTO() {
        return fileDTO;
    }

    public void setFileDTO(FileDTO fileDTO) {
        this.fileDTO = fileDTO;
    }

    public List<FileDTO> getFileDTOs() {
        return fileDTOs;
    }

    public void setFileDTOs(List<FileDTO> fileDTOs) {
        this.fileDTOs = fileDTOs;
    }
}
