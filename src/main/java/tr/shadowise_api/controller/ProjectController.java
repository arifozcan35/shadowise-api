package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.IResult;
import tr.shadowise_api.dto.request.ProjectCreateRequestDto;
import tr.shadowise_api.entity.Project;
import tr.shadowise_api.entity.User;
import tr.shadowise_api.service.ProjectService;
import tr.shadowise_api.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        IDataResult<List<Project>> result = projectService.getAllProjects();
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id) {
        IDataResult<Project> result = projectService.getProjectById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/my-projects")
    public ResponseEntity<?> getMyProjects() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
            }
            
            IDataResult<List<Project>> result = projectService.getProjectsByOwner(currentUser);
            if (result.isSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error retrieving user projects: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
            }
            
            IDataResult<Project> result = projectService.createProject(projectCreateRequestDto, currentUser);
            if (result.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating project: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable String id, 
                                         @RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        IDataResult<Project> result = projectService.updateProject(id, projectCreateRequestDto);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<?> updateProjectTags(@PathVariable String id, @RequestBody String[] tags) {
        IDataResult<Project> result = projectService.updateProjectTags(id, tags);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PutMapping("/{id}/documents")
    public ResponseEntity<?> updateProjectDocuments(@PathVariable String id, @RequestBody String[] documents) {
        IDataResult<Project> result = projectService.updateProjectDocuments(id, documents);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PutMapping("/{id}/insights")
    public ResponseEntity<?> updateProjectInsights(@PathVariable String id, @RequestBody String[] insights) {
        IDataResult<Project> result = projectService.updateProjectInsights(id, insights);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        IResult result = projectService.deleteProject(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteProject(@PathVariable String id) {
        IResult result = projectService.softDeleteProject(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getProjectCount() {
        long count = projectService.getProjectCount();
        return ResponseEntity.ok("Total projects: " + count);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<?> checkProjectExists(@PathVariable String id) {
        boolean exists = projectService.projectExists(id);
        return ResponseEntity.ok("Project exists: " + exists);
    }
}
