package com.mertncu.UniversityClubManagementFramework.service;

import com.mertncu.UniversityClubManagementFramework.dto.TaskDTO;
import com.mertncu.UniversityClubManagementFramework.entity.Task;
import com.mertncu.UniversityClubManagementFramework.exception.ResourceNotFoundException;
import com.mertncu.UniversityClubManagementFramework.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
    }

    public List<Task> getTasksByClubId(int clubId) {
        return taskRepository.findByClubId(clubId);
    }

    public List<Task> getTasksByAssignedUser(String userId) {
        return taskRepository.findByAssignedTo(userId);
    }

    public Task updateTask(int taskId, Task taskDetails) {
        Task task = getTaskById(taskId); // Fetch the existing task

        // Update the fields of the existing task with the new values from taskDetails
        task.setTaskName(taskDetails.getTaskName());
        task.setTaskDescription(taskDetails.getTaskDescription());
        task.setAssignedTo(taskDetails.getAssignedTo());
        task.setDueDate(taskDetails.getDueDate());
        task.setStatus(taskDetails.getStatus());
        task.setClubId(taskDetails.getClubId());

        return taskRepository.save(task); // Save the updated task
    }

    public void deleteTask(int taskId) {
        Task task = getTaskById(taskId);
        taskRepository.delete(task);
    }
}