package com.example.hahn_internship.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request,
                                                         @AuthenticationPrincipal User user) {
        Project project = projectService.createProject(request, user);
        return ResponseEntity.ok(new ProjectResponse(project.getId(), project.getTitle(), project.getDescription()));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getUserProjects(@AuthenticationPrincipal User user) {
        List<ProjectResponse> projects = projectService.getUserProjects(user)
                .stream()
                .map(p -> new ProjectResponse(p.getId(), p.getTitle(), p.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

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
