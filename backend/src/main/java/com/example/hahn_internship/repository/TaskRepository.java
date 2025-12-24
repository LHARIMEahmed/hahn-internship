package com.example.hahn_internship.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hahn_internship.model.Task;

/**
 * Repository interface for managing {@link Task} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds all tasks associated with a specific project.
     *
     * @param projectId the ID of the project
     * @return a list of tasks belonging to the project
     */
    List<Task> findByProjectId(Long projectId);
}
