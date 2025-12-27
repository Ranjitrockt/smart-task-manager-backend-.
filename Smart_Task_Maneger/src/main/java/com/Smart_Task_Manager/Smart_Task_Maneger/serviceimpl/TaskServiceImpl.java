package com.Smart_Task_Manager.Smart_Task_Maneger.serviceimpl;

import com.Smart_Task_Manager.Smart_Task_Maneger.entity.Task;
import com.Smart_Task_Manager.Smart_Task_Maneger.entity.TaskHistory;
import com.Smart_Task_Manager.Smart_Task_Maneger.repository.TaskHistoryRepository;
import com.Smart_Task_Manager.Smart_Task_Maneger.repository.TaskRepository;
import com.Smart_Task_Manager.Smart_Task_Maneger.service.TaskService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskHistoryRepository historyRepository,
                           ObjectMapper objectMapper) {
        this.taskRepository = taskRepository;
        this.historyRepository = historyRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<TaskHistory> getTaskHistory(UUID taskId) {
        return historyRepository.findByTaskIdOrderByChangedAtDesc(taskId);
    }

    @Override
    public Task createTask(Task task) {
        String text = (task.getTitle() + " " + (task.getDescription() != null ? task.getDescription() : "")).toLowerCase();

        // Auto classification
        if (task.getCategory() == null || task.getCategory().isEmpty()) {
            task.setCategory(detectCategory(text));
        }
        if (task.getPriority() == null || task.getPriority().isEmpty()) {
            task.setPriority(detectPriority(text));
        }
        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("pending");
        }
        if (task.getSuggestedActions() == null || task.getSuggestedActions().isEmpty()) {
            task.setSuggestedActions(getSuggestedActions(task.getCategory()));
        }

        Task savedTask = taskRepository.save(task);
        
        // Record history
        recordHistory(savedTask.getId(), "CREATE", null, savedTask);
        
        return savedTask;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public Task updateTask(UUID id, Task task) {
        Task existing = getTaskById(id);
        
        // Create a copy of the existing task for history (before update)
        Map<String, Object> oldValueMap = convertToMap(existing);

        if (task.getTitle() != null) existing.setTitle(task.getTitle());
        if (task.getDescription() != null) existing.setDescription(task.getDescription());
        if (task.getAssignedTo() != null) existing.setAssignedTo(task.getAssignedTo());
        if (task.getDueDate() != null) existing.setDueDate(task.getDueDate());
        if (task.getStatus() != null) existing.setStatus(task.getStatus());
        if (task.getPriority() != null) existing.setPriority(task.getPriority());
        if (task.getCategory() != null) existing.setCategory(task.getCategory());
        if (task.getSuggestedActions() != null) existing.setSuggestedActions(task.getSuggestedActions());
        if (task.getExtractedEntities() != null) existing.setExtractedEntities(task.getExtractedEntities());

        Task updatedTask = taskRepository.save(existing);
        
        // Record history
        recordHistory(updatedTask.getId(), "UPDATE", oldValueMap, updatedTask);

        return updatedTask;
    }

    @Override
    public void deleteTask(UUID id) {
        Task existing = getTaskById(id);
        Map<String, Object> oldValueMap = convertToMap(existing);
        
        taskRepository.deleteById(id);
        
        // Record history (newValue is null for delete)
        recordHistory(id, "DELETE", oldValueMap, null);
    }

    private void recordHistory(UUID taskId, String action, Object oldValue, Object newValue) {
        try {
            Map<String, Object> oldVal;
            if (oldValue instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> casted = (Map<String, Object>) oldValue;
                oldVal = casted;
            } else {
                oldVal = convertToMap(oldValue);
            }
            
            Map<String, Object> newVal = convertToMap(newValue);

            TaskHistory history = TaskHistory.builder()
                    .taskId(taskId)
                    .action(action)
                    .oldValue(oldVal)
                    .newValue(newVal)
                    .changedBy("system") // You might want to get the current user here
                    .build();
            
            historyRepository.save(history);
        } catch (Exception e) {
            logger.error("Failed to record task history for task {}", taskId, e);
        }
    }

    private Map<String, Object> convertToMap(Object object) {
        if (object == null) return null;
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }

    private String detectCategory(String text) {
        if (text == null) return "general";
        if (containsAny(text, "meeting", "schedule", "call", "appointment", "deadline")) {
            return "scheduling";
        }
        if (containsAny(text, "payment", "invoice", "bill", "budget", "cost", "expense")) {
            return "finance";
        }
        if (containsAny(text, "bug", "fix", "error", "install", "repair", "maintain")) {
            return "technical";
        }
        if (containsAny(text, "safety", "hazard", "inspection", "compliance", "ppe")) {
            return "safety";
        }
        return "general";
    }

    private String detectPriority(String text) {
        if (text == null) return "low";
        if (containsAny(text, "urgent", "asap", "immediately", "today", "critical", "emergency")) {
            return "high";
        }
        if (containsAny(text, "soon", "this week", "important")) {
            return "medium";
        }
        return "low";
    }

    private List<String> getSuggestedActions(String category) {
        if (category == null) return List.of("Review task", "Assign owner");
        return switch (category) {
            case "scheduling" -> List.of(
                    "Block calendar",
                    "Send invite",
                    "Prepare agenda",
                    "Set reminder"
            );
            case "finance" -> List.of(
                    "Check budget",
                    "Get approval",
                    "Generate invoice"
            );
            case "technical" -> List.of(
                    "Diagnose issue",
                    "Assign technician",
                    "Document fix"
            );
            case "safety" -> List.of(
                    "Conduct inspection",
                    "File report",
                    "Notify supervisor"
            );
            default -> List.of("Review task", "Assign owner");
        };
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Page<Task> getAllTasks(int page, int size, String sortBy, String status, String category, String priority) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).descending()
        );

        Specification<Task> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (category != null && !category.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
            if (priority != null && !priority.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return taskRepository.findAll(spec, pageable);
    }
}
