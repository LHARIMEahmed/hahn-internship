package com.example.hahn_internship.repository; 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hahn_internship.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
}
