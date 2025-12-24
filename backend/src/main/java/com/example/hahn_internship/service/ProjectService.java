package com.example.hahn_internship.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.hahn_internship.dto.ProjectRequest;
import com.example.hahn_internship.model.Project;
import com.example.hahn_internship.model.User;
import com.example.hahn_internship.repository.ProjectRepository;
import com.example.hahn_internship.repository.UserRepository;

/**
 * Service class for managing projects.
 * Handles CRUD operations and ensures projects are linked to the authenticated user.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new project for the authenticated user.
     *
     * @param request contains project title and description
     * @param userDetails the authenticated user's details
     * @return the created Project entity
     * @throws IllegalArgumentException if project title is empty
     * @throws RuntimeException if the user is not found
     */
    public Project createProject(ProjectRequest request, UserDetails userDetails) {
        if (!StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("Project title cannot be empty");
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setUser(user);
        return projectRepository.save(project);
    }

    /**
     * Retrieves all projects for a specific user.
     *
     * @param user the user whose projects should be fetched
     * @return list of projects
     */
    public List<Project> getUserProjects(User user) {
        return projectRepository.findByUser(user);
    }

    /**
     * Retrieves a project by ID only if it belongs to the given user.
     *
     * @param projectId the project ID
     * @param user the owner of the project
     * @return the Project entity
     * @throws ResponseStatusException if the project is not found for the user
     */
    public Project getProjectByIdAndUser(Long projectId, User user) {
        return projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project with ID " + projectId + " not found for user " + user.getEmail()
                ));
    }

    /**
     * Updates a project (only allowed for the owner).
     *
     * @param projectId the project ID
     * @param request contains new title and/or description
     * @param user the authenticated user
     * @return the updated Project entity
     */
    public Project updateProject(Long projectId, ProjectRequest request, User user) {
        Project project = getProjectByIdAndUser(projectId, user);
        if (StringUtils.hasText(request.getTitle())) {
            project.setTitle(request.getTitle());
        }
        project.setDescription(request.getDescription());
        return projectRepository.save(project);
    }

    /**
     * Deletes a project (only allowed for the owner).
     *
     * @param projectId the project ID
     * @param user the authenticated user
     */
    public void deleteProject(Long projectId, User user) {
        Project project = getProjectByIdAndUser(projectId, user);
        projectRepository.delete(project);
    }
}
