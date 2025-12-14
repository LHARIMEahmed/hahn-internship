package com.example.hahn_internship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hahn_internship.model.Project;
import com.example.hahn_internship.model.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
    Optional<Project> findByIdAndUser(Long id, User user);
}
