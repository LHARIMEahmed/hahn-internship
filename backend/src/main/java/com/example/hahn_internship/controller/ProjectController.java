package com.example.hahn_internship.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hahn_internship.dto.ProjectRequest;
import com.example.hahn_internship.dto.ProjectResponse;
import com.example.hahn_internship.model.Project;
import com.example.hahn_internship.model.User;
import com.example.hahn_internship.repository.UserRepository;
import com.example.hahn_internship.service.ProjectService;

/**
 * REST controller responsible for project-related endpoints.
 * Supports creating, retrieving, updating, and deleting projects.
 * Only accessible to authenticated users.
 */
@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new project for the authenticated user.
     *
     * @param request     the project creation request containing title and description
     * @param userDetails the authenticated user details injected by Spring Security
     * @return the created project as a ProjectResponse
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Project project = projectService.createProject(request, userDetails);
        return ResponseEntity.ok(new ProjectResponse(project.getId(), project.getTitle(), project.getDescription()));
    }

    /**
     * Retrieves all projects belonging to the authenticated user.
     *
     * @param userDetails the authenticated user details
     * @return a list of ProjectResponse objects
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getUserProjects(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ProjectResponse> projects = projectService.getUserProjects(user)
                .stream()
                .map(p -> new ProjectResponse(p.getId(), p.getTitle(), p.getDescription()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(projects);
    }

    /**
     * Retrieves a specific project by ID for the authenticated user.
     *
     * @param id          the project ID
     * @param userDetails the authenticated user details
     * @return the requested Project entity
     */
    @GetMapping("/{id}")
    public Project getProject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow();

        return projectService.getProjectByIdAndUser(id, user);
    }

    /**
     * Updates a specific project by ID for the authenticated user.
     *
     * @param id          the project ID
     * @param request     the project update request
     * @param userDetails the authenticated user details
     * @return the updated Project entity
     */
    @PutMapping("/{id}")
    public Project updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow();

        return projectService.updateProject(id, request, user);
    }

    /**
     * Deletes a specific project by ID for the authenticated user.
     *
     * @param id          the project ID
     * @param userDetails the authenticated user details
     * @return ResponseEntity with no content on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        projectService.deleteProject(id, user);
        return ResponseEntity.noContent().build();
    }
}
