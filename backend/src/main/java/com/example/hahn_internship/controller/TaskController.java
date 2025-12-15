package com.example.hahn_internship.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Create a task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskRequest request) {
        TaskResponse taskResponse = taskService.createTask(projectId, request);
        return ResponseEntity.ok(taskResponse);
    }

    // List all tasks for a project
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable Long projectId) {
        List<TaskResponse> tasks = taskService.getTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    // Mark task as completed
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponse> markTaskCompleted(
            @PathVariable Long taskId) {
        TaskResponse taskResponse = taskService.markTaskAsCompleted(taskId);
        return ResponseEntity.ok(taskResponse);
    }

    // Delete a task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
