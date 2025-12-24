package com.example.hahn_internship.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hahn_internship.dto.TaskRequest;
import com.example.hahn_internship.dto.TaskResponse;
import com.example.hahn_internship.model.Project;
import com.example.hahn_internship.model.Task;
import com.example.hahn_internship.repository.ProjectRepository;
import com.example.hahn_internship.repository.TaskRepository;

/**
 * Service class for managing tasks within projects.
 * Provides methods for creating, retrieving, updating, completing, and deleting tasks.
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Creates a new task within a project.
     *
     * @param projectId the ID of the project
     * @param request task details (title, description, due date)
     * @return the created TaskResponse
     * @throws RuntimeException if the project does not exist
     */
    public TaskResponse createTask(Long projectId, TaskRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setCompleted(false);
        task.setProject(project);

        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
            savedTask.getId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            savedTask.getDueDate(),
            savedTask.isCompleted()
        );
    }

    /**
     * Retrieves all tasks for a given project.
     *
     * @param projectId the ID of the project
     * @return list of TaskResponse objects
     * @throws RuntimeException if the project does not exist
     */
    public List<TaskResponse> getTasks(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Task> tasks = taskRepository.findByProjectId(project.getId());

        return tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.isCompleted()
                ))
                .toList();
    }

    /**
     * Marks a task as completed.
     *
     * @param taskId the ID of the task
     * @return updated TaskResponse
     * @throws RuntimeException if the task does not exist
     */
    public TaskResponse markTaskAsCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);

        return new TaskResponse(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getDueDate(),
            updatedTask.isCompleted()
        );
    }

    /**
     * Deletes a task.
     *
     * @param taskId the ID of the task
     * @throws RuntimeException if the task does not exist
     */
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    /**
     * Updates a task's details.
     *
     * @param taskId the ID of the task
     * @param request task details to update (title, description, due date)
     * @return updated TaskResponse
     * @throws RuntimeException if the task does not exist
     */
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        // optionally handle completed status
        taskRepository.save(task);

        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getDueDate(),
            task.isCompleted()
        );
    }
}
