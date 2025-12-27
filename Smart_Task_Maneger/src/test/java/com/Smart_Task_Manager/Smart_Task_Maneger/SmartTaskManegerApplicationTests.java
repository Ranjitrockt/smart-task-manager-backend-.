package com.Smart_Task_Manager.Smart_Task_Maneger;

import com.Smart_Task_Manager.Smart_Task_Maneger.entity.Task;
import com.Smart_Task_Manager.Smart_Task_Maneger.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SmartTaskManegerApplicationTests {

    @Autowired
    private TaskService taskService;

    @Test
    void contextLoads() {
    }

    @Test
    void testAutoClassificationScheduling() {
        Task task = new Task();
        task.setTitle("Schedule a meeting");
        task.setDescription("We need to schedule a meeting with the team.");
        
        Task createdTask = taskService.createTask(task);
        
        assertEquals("scheduling", createdTask.getCategory());
        assertNotNull(createdTask.getSuggestedActions());
        assertTrue(createdTask.getSuggestedActions().contains("Block calendar"));
    }

    @Test
    void testAutoClassificationFinance() {
        Task task = new Task();
        task.setTitle("Pay the invoice");
        task.setDescription("The invoice for the new software is due.");
        
        Task createdTask = taskService.createTask(task);
        
        assertEquals("finance", createdTask.getCategory());
        assertNotNull(createdTask.getSuggestedActions());
        assertTrue(createdTask.getSuggestedActions().contains("Check budget"));
    }

    @Test
    void testPriorityDetectionHigh() {
        Task task = new Task();
        task.setTitle("Urgent bug fix");
        task.setDescription("This is a critical bug that needs to be fixed asap.");
        
        Task createdTask = taskService.createTask(task);
        
        assertEquals("high", createdTask.getPriority());
    }
    
    @Test
    void testPriorityDetectionMedium() {
        Task task = new Task();
        task.setTitle("Review code");
        task.setDescription("Please review the code this week.");
        
        Task createdTask = taskService.createTask(task);
        
        assertEquals("medium", createdTask.getPriority());
    }
}
