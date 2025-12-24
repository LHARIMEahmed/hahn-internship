package com.example.hahn_internship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hahn_internship.model.Project;
import com.example.hahn_internship.model.User;

/**
 * Repository interface for managing {@link Project} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Finds all projects that belong to a specific user.
     *
     * @param user the user whose projects are to be retrieved
     * @return a list of projects associated with the given user
     */
    List<Project> findByUser(User user);

    /**
     * Finds a project by its ID and ensures it belongs to the specified user.
     *
     * @param id   the ID of the project
     * @param user the user who owns the project
     * @return an Optional containing the project if found, otherwise empty
     */
    Optional<Project> findByIdAndUser(Long id, User user);
}
