package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.core.response.*;
import tr.shadowise_api.dto.request.ProjectCreateRequestDto;
import tr.shadowise_api.dto.response.DashboardStatsResponseDto;
import tr.shadowise_api.entity.Project;
import tr.shadowise_api.entity.UploadedFile;
import tr.shadowise_api.entity.User;
import tr.shadowise_api.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    /**
     * Create a new project from ProjectCreateRequestDto
     */
    public IDataResult<Project> createProject(ProjectCreateRequestDto requestDto, User owner) {
        try {
            Project project = new Project();
            project.setTitle(requestDto.getTitle());
            project.setDescription(requestDto.getDescription());
            project.setUploadedFiles(requestDto.getUploadedFiles());
            project.setOwner(owner);
            project.setCreatedAt(LocalDateTime.now());
            project.setUpdatedAt(LocalDateTime.now());
            
            // Initialize empty arrays for tags, documents, and insights
            project.setTags(new String[0]);
            project.setDocuments(new String[0]);
            project.setInsights(new String[0]);
            
            Project savedProject = projectRepository.save(project);
            return new SuccessDataResult<>(savedProject, "Project created successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to create project: " + e.getMessage());
        }
    }

    /**
     * Get all projects
     */
    public IDataResult<List<Project>> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return new SuccessDataResult<>(projects, "Projects retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve projects: " + e.getMessage());
        }
    }

    /**
     * Get project by ID
     */
    public IDataResult<Project> getProjectById(String id) {
        try {
            Optional<Project> project = projectRepository.findById(id);
            if (project.isPresent()) {
                return new SuccessDataResult<>(project.get(), "Project retrieved successfully");
            } else {
                return new ErrorDataResult<>(null, "Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve project: " + e.getMessage());
        }
    }

    /**
     * Get projects by owner
     */
    public IDataResult<List<Project>> getProjectsByOwner(User owner) {
        try {
            List<Project> projects = projectRepository.findAll().stream()
                    .filter(project -> project.getOwner() != null && 
                            project.getOwner().getId().equals(owner.getId()))
                    .toList();
            return new SuccessDataResult<>(projects, "Projects retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve projects: " + e.getMessage());
        }
    }

    /**
     * Create a new project with title and description
     */
    public IDataResult<Project> createProject(String title, String description, User owner) {
        try {
            Project project = new Project();
            project.setTitle(title);
            project.setDescription(description);
            project.setOwner(owner);
            project.setCreatedAt(LocalDateTime.now());
            project.setUpdatedAt(LocalDateTime.now());
            
            // Initialize empty arrays for tags, documents, and insights
            project.setTags(new String[0]);
            project.setDocuments(new String[0]);
            project.setInsights(new String[0]);
            project.setUploadedFiles(new ArrayList<>()); // Initialize empty list for uploaded files
            
            Project savedProject = projectRepository.save(project);
            return new SuccessDataResult<>(savedProject, "Project created successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to create project: " + e.getMessage());
        }
    }
    
    /**
     * Update project files
     */
    public IDataResult<Project> updateProjectFiles(String id, List<UploadedFile> uploadedFiles) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project project = existingProject.get();
                project.setUploadedFiles(uploadedFiles);
                project.setUpdatedAt(LocalDateTime.now());
                
                Project updatedProject = projectRepository.save(project);
                return new SuccessDataResult<>(updatedProject, "Project files updated successfully");
            } else {
                return new ErrorDataResult<>(null, "Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update project files: " + e.getMessage());
        }
    }

    /**
     * Update project
     */
    public IDataResult<Project> updateProject(String id, ProjectCreateRequestDto requestDto) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project project = existingProject.get();
                project.setTitle(requestDto.getTitle());
                project.setDescription(requestDto.getDescription());
                project.setUploadedFiles(requestDto.getUploadedFiles());
                project.setUpdatedAt(LocalDateTime.now());
                
                Project updatedProject = projectRepository.save(project);
                return new SuccessDataResult<>(updatedProject, "Project updated successfully");
            } else {
                return new ErrorDataResult<>(null, "Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update project: " + e.getMessage());
        }
    }

    /**
     * Update project tags
     */
    public IDataResult<Project> updateProjectTags(String id, String[] tags) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project project = existingProject.get();
                project.setTags(tags);
                project.setUpdatedAt(LocalDateTime.now());
                
                Project updatedProject = projectRepository.save(project);
                return new SuccessDataResult<>(updatedProject, "Project tags updated successfully");
            } else {
                return new ErrorDataResult<>(null, "Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update project tags: " + e.getMessage());
        }
    }

    /**
     * Update project documents
     */
    public IDataResult<Project> updateProjectDocuments(String id, String[] documents) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project project = existingProject.get();
                project.setDocuments(documents);
                project.setUpdatedAt(LocalDateTime.now());
                
                Project updatedProject = projectRepository.save(project);
                return new SuccessDataResult<>(updatedProject, "Project documents updated successfully");
            } else {
                return new ErrorDataResult<>(null, "Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update project documents: " + e.getMessage());
        }
    }

    /**
     * Update project insights
     */
    public IDataResult<Project> updateProjectInsights(String id, String[] insights) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project project = existingProject.get();
                project.setInsights(insights);
                project.setUpdatedAt(LocalDateTime.now());
                
                Project updatedProject = projectRepository.save(project);
                return new SuccessDataResult<>(updatedProject, "Project insights updated successfully");
            } else {
                return new ErrorDataResult<>(null, "Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to update project insights: " + e.getMessage());
        }
    }

    /**
     * Soft delete project
     */
    public IResult softDeleteProject(String id) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project project = existingProject.get();
                project.setDeletedAt(LocalDateTime.now());
                project.setUpdatedAt(LocalDateTime.now());
                
                projectRepository.save(project);
                return new SuccessResult("Project soft deleted successfully");
            } else {
                return new ErrorResult("Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to soft delete project: " + e.getMessage());
        }
    }

    /**
     * Hard delete project
     */
    public IResult deleteProject(String id) {
        try {
            if (projectRepository.existsById(id)) {
                projectRepository.deleteById(id);
                return new SuccessResult("Project deleted successfully");
            } else {
                return new ErrorResult("Project not found with id: " + id);
            }
        } catch (Exception e) {
            return new ErrorResult("Failed to delete project: " + e.getMessage());
        }
    }

    /**
     * Check if project exists
     */
    public boolean projectExists(String id) {
        return projectRepository.existsById(id);
    }

    /**
     * Get project count
     */
    public long getProjectCount() {
        return projectRepository.count();
    }
    
    /**
     * Get dashboard statistics
     * Returns total projects count and total documents count
     */
    public IDataResult<DashboardStatsResponseDto> getDashboardStats() {
        try {
            List<Project> projects = projectRepository.findAll();
            long totalProjects = projects.size();
            
            // Calculate total documents across all projects
            long totalDocuments = projects.stream()
                    .filter(project -> project.getDocuments() != null)
                    .mapToLong(project -> project.getDocuments().length)
                    .sum();
            
            DashboardStatsResponseDto stats = new DashboardStatsResponseDto(totalProjects, totalDocuments);
            return new SuccessDataResult<>(stats, "Dashboard statistics retrieved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to retrieve dashboard statistics: " + e.getMessage());
        }
    }
}
