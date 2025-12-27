package com.Smart_Task_Manager.Smart_Task_Maneger.service;

import com.Smart_Task_Manager.Smart_Task_Maneger.entity.Task;
import com.Smart_Task_Manager.Smart_Task_Maneger.entity.TaskHistory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TaskService {


        Task createTask(Task task);

        List<Task> getAllTasks();

        Task getTaskById(UUID id);

        Task updateTask(UUID id, Task task);

        void deleteTask(UUID id);
        
        List<TaskHistory> getTaskHistory(UUID taskId);
        
        Page<Task> getAllTasks(int page, int size, String sortBy, String status, String category, String priority);
}
