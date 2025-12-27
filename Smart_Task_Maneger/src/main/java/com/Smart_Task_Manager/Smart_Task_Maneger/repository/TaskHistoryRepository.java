package com.Smart_Task_Manager.Smart_Task_Maneger.repository;

import com.Smart_Task_Manager.Smart_Task_Maneger.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, UUID> {

    List<TaskHistory> findByTaskIdOrderByChangedAtDesc(UUID taskId);
}
