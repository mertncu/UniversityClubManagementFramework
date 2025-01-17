package com.mertncu.UniversityClubManagementFramework.repository.task;

import com.mertncu.UniversityClubManagementFramework.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByClubId(int clubId);
    List<Task> findByAssignedTo(String userId);
}