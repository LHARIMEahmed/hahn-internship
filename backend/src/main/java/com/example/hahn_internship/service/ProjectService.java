package com.example.hahn_internship.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.hahn_internship.dto.ProjectRequest;
import com.example.hahn_internship.model.Project;
import com.example.hahn_internship.model.User;
import com.example.hahn_internship.repository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Create a new project for a user
     */
    public Project createProject(ProjectRequest request, User user) {
        if (!StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("Project title cannot be empty");
        }

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setUser(user);
        return projectRepository.save(project);
    }

    /**
     * Get all projects for a specific user
     */
    public List<Project> getUserProjects(User user) {
        return projectRepository.findByUser(user);
    }

    /**
     * Get a specific project by id, only if it belongs to the given user
     */
    public Project getProjectByIdAndUser(Long projectId, User user) {
    return projectRepository.findByIdAndUser(projectId, user)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Project with ID " + projectId + " not found for user " + user.getEmail()
            ));
}

    /**
     * Update project (only for owner)
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
     * Delete project (only for owner)
     */
    public void deleteProject(Long projectId, User user) {
        Project project = getProjectByIdAndUser(projectId, user);
        projectRepository.delete(project);
    }
}
