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

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;   
    // Créer une tâche dans un projet
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

    // Lister toutes les tâches d’un projet
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

    // Marquer une tâche comme terminée
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

    // Supprimer une tâche
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }
}
