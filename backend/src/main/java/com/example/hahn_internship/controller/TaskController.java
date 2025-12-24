package com.example.hahn_internship.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hahn_internship.dto.TaskRequest;
import com.example.hahn_internship.dto.TaskResponse;
import com.example.hahn_internship.service.TaskService;

/**
 * REST controller responsible for managing tasks within projects.
 * Provides endpoints for creating, updating, completing, listing, and deleting tasks.
 */
@RestController
@RequestMapping("/projects/{projectId}/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Creates a new task within a project.
     *
     * @param projectId the ID of the project
     * @param request   the task creation request containing title, description, etc.
     * @return the created TaskResponse
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskRequest request) {
        TaskResponse taskResponse = taskService.createTask(projectId, request);
        return ResponseEntity.ok(taskResponse);
    }

    /**
     * Retrieves all tasks for a given project.
     *
     * @param projectId the ID of the project
     * @return list of TaskResponse objects
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable Long projectId) {
        List<TaskResponse> tasks = taskService.getTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Marks a task as completed.
     *
     * @param taskId the ID of the task
     * @return the updated TaskResponse with completed status
     */
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponse> markTaskCompleted(
            @PathVariable Long taskId) {
        TaskResponse taskResponse = taskService.markTaskAsCompleted(taskId);
        return ResponseEntity.ok(taskResponse);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing task.
     *
     * @param taskId  the ID of the task
     * @param request the updated task data
     * @return the updated TaskResponse
     */
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponse> editTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequest request) {
        TaskResponse updatedTask = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
    }
}
