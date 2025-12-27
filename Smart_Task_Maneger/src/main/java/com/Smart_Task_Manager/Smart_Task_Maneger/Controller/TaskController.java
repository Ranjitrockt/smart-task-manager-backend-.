package com.Smart_Task_Manager.Smart_Task_Maneger.Controller;



import com.Smart_Task_Manager.Smart_Task_Maneger.entity.Task;
import com.Smart_Task_Manager.Smart_Task_Maneger.entity.TaskHistory;
import com.Smart_Task_Manager.Smart_Task_Maneger.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/{id}/history")
    public List<TaskHistory> getTaskHistory(@PathVariable UUID id) {
        return taskService.getTaskHistory(id);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable UUID id,
            @RequestBody Task task) {

        return ResponseEntity.ok(taskService.updateTask(id, task));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @GetMapping
    public Map<String, Object> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priority
    ) {
        Page<Task> taskPage = taskService.getAllTasks(page, size, sortBy, status, category, priority);

        return Map.of(
                "content", taskPage.getContent(),
                "totalPages", taskPage.getTotalPages(),
                "totalElements", taskPage.getTotalElements(),
                "page", taskPage.getNumber()
        );
    }




}
