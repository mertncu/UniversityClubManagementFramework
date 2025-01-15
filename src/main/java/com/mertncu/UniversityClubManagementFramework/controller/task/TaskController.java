package com.mertncu.UniversityClubManagementFramework.controller.task;

import com.mertncu.UniversityClubManagementFramework.dto.auth.AuthReqResDTO;
import com.mertncu.UniversityClubManagementFramework.dto.task.TaskDTO;
import com.mertncu.UniversityClubManagementFramework.entity.task.Task;
import com.mertncu.UniversityClubManagementFramework.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mertncu.UniversityClubManagementFramework.service.user.UserManagementService;
import com.mertncu.UniversityClubManagementFramework.service.club.ClubService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ClubService clubService;

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/tasks/createTask")
    public ResponseEntity<AuthReqResDTO> createTask(@RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setClubId(taskDTO.getClubId());
        task.setTaskName(taskDTO.getTaskName());
        task.setTaskDescription(taskDTO.getTaskDescription());
        task.setAssignedTo(taskDTO.getAssignedTo());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatus(taskDTO.getStatus());

        Task newTask = taskService.saveTask(task);

        TaskDTO createdTaskDTO = new TaskDTO();
        createdTaskDTO.setTaskId(newTask.getTaskId());
        createdTaskDTO.setClubId(newTask.getClubId());
        createdTaskDTO.setTaskName(newTask.getTaskName());
        createdTaskDTO.setTaskDescription(newTask.getTaskDescription());
        createdTaskDTO.setAssignedTo(newTask.getAssignedTo());
        createdTaskDTO.setDueDate(newTask.getDueDate());
        createdTaskDTO.setStatus(newTask.getStatus());

        // Fetch and set the club name and user name
        var club = clubService.getClubById(newTask.getClubId());
        if (club != null) {
            createdTaskDTO.setClubName(club.getName());
        }

        var user = userManagementService.getUserById(newTask.getAssignedTo());
        if(user != null){
            createdTaskDTO.setAssignedToName(user.getUser().getName());
        }

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Task created successfully");
        response.setTaskDTO(createdTaskDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/tasks/getAllTasks")
    public ResponseEntity<AuthReqResDTO> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();

        // Map Task objects to TaskDTOs
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setTaskId(task.getTaskId());
                    dto.setClubId(task.getClubId());
                    dto.setTaskName(task.getTaskName());
                    dto.setTaskDescription(task.getTaskDescription());
                    dto.setAssignedTo(task.getAssignedTo());
                    dto.setDueDate(task.getDueDate());
                    dto.setStatus(task.getStatus());

                    // Fetch and set the club name
                    var club = clubService.getClubById(task.getClubId());
                    if (club != null) {
                        dto.setClubName(club.getName());
                    }

                    // Fetch and set the user name
                    var user = userManagementService.getUserById(task.getAssignedTo());
                    if (user != null) {
                        dto.setAssignedToName(user.getUser().getName());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setTaskDTOs(taskDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tasks/getTask/{taskId}")
    public ResponseEntity<AuthReqResDTO> getTaskById(@PathVariable int taskId) {
        Task task = taskService.getTaskById(taskId);

        // Create TaskDTO
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setClubId(task.getClubId());
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setTaskDescription(task.getTaskDescription());
        taskDTO.setAssignedTo(task.getAssignedTo());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setStatus(task.getStatus());

        // Fetch and set the club name and user name
        var club = clubService.getClubById(task.getClubId());
        if (club != null) {
            taskDTO.setClubName(club.getName());
        }

        var user = userManagementService.getUserById(task.getAssignedTo());
        if (user != null) {
            taskDTO.setAssignedToName(user.getUser().getName());
        }

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setTaskDTO(taskDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tasks/getTasksByClubId/{clubId}")
    public ResponseEntity<AuthReqResDTO> getTasksByClubId(@PathVariable int clubId) {
        List<Task> tasks = taskService.getTasksByClubId(clubId);

        // Map to TaskDTOs
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setTaskId(task.getTaskId());
                    dto.setClubId(task.getClubId());
                    dto.setTaskName(task.getTaskName());
                    dto.setTaskDescription(task.getTaskDescription());
                    dto.setAssignedTo(task.getAssignedTo());
                    dto.setDueDate(task.getDueDate());
                    dto.setStatus(task.getStatus());

                    // Fetch and set the club name
                    var club = clubService.getClubById(task.getClubId());
                    if (club != null) {
                        dto.setClubName(club.getName());
                    }

                    // Fetch and set the user name
                    var user = userManagementService.getUserById(task.getAssignedTo());
                    if (user != null) {
                        dto.setAssignedToName(user.getUser().getName());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setTaskDTOs(taskDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tasks/getTasksByUser/{userId}")
    public ResponseEntity<AuthReqResDTO> getTasksByUser(@PathVariable String userId) {
        List<Task> tasks = taskService.getTasksByAssignedUser(userId);

        // Map to TaskDTOs
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setTaskId(task.getTaskId());
                    dto.setClubId(task.getClubId());
                    dto.setTaskName(task.getTaskName());
                    dto.setTaskDescription(task.getTaskDescription());
                    dto.setAssignedTo(task.getAssignedTo());
                    dto.setDueDate(task.getDueDate());
                    dto.setStatus(task.getStatus());

                    // Fetch and set the club name
                    var club = clubService.getClubById(task.getClubId());
                    if (club != null) {
                        dto.setClubName(club.getName());
                    }

                    // Fetch and set the user name
                    var user = userManagementService.getUserById(task.getAssignedTo());
                    if (user != null) {
                        dto.setAssignedToName(user.getUser().getName());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setTaskDTOs(taskDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/tasks/updateTask/{taskId}")
    public ResponseEntity<AuthReqResDTO> updateTask(@PathVariable int taskId, @RequestBody TaskDTO taskDTO) {
        Task existingTask = taskService.getTaskById(taskId);

        // Update the existing task with data from DTO
        existingTask.setClubId(taskDTO.getClubId());
        existingTask.setTaskName(taskDTO.getTaskName());
        existingTask.setTaskDescription(taskDTO.getTaskDescription());
        existingTask.setAssignedTo(taskDTO.getAssignedTo());
        existingTask.setDueDate(taskDTO.getDueDate());
        existingTask.setStatus(taskDTO.getStatus());

        // Convert TaskDTO to Task entity
        Task taskToUpdate = new Task();
        taskToUpdate.setTaskId(taskId); // Assuming you want to preserve the ID
        taskToUpdate.setClubId(taskDTO.getClubId());
        taskToUpdate.setTaskName(taskDTO.getTaskName());
        taskToUpdate.setTaskDescription(taskDTO.getTaskDescription());
        taskToUpdate.setAssignedTo(taskDTO.getAssignedTo());
        taskToUpdate.setDueDate(taskDTO.getDueDate());
        taskToUpdate.setStatus(taskDTO.getStatus());

        // Call service method with Task entity
        Task updatedTask = taskService.updateTask(taskId, taskToUpdate);

        // Create a TaskDTO from the updated Task
        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setTaskId(updatedTask.getTaskId());
        updatedTaskDTO.setClubId(updatedTask.getClubId());
        updatedTaskDTO.setTaskName(updatedTask.getTaskName());
        updatedTaskDTO.setTaskDescription(updatedTask.getTaskDescription());
        updatedTaskDTO.setAssignedTo(updatedTask.getAssignedTo());
        updatedTaskDTO.setDueDate(updatedTask.getDueDate());
        updatedTaskDTO.setStatus(updatedTask.getStatus());

        // Fetch and set the club name and user name
        var club = clubService.getClubById(updatedTask.getClubId());
        if (club != null) {
            updatedTaskDTO.setClubName(club.getName());
        }

        var user = userManagementService.getUserById(updatedTask.getAssignedTo());
        if (user != null) {
            updatedTaskDTO.setAssignedToName(user.getUser().getName());
        }

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Task updated successfully");
        response.setTaskDTO(updatedTaskDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/deleteTask/{taskId}")
    public ResponseEntity<AuthReqResDTO> deleteTask(@PathVariable int taskId) {
        taskService.deleteTask(taskId);

        AuthReqResDTO response = new AuthReqResDTO();
        response.setStatusCode(HttpStatus.NO_CONTENT.value());
        response.setMessage("Task deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}