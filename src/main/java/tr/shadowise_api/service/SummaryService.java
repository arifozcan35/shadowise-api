package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.core.response.ErrorDataResult;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.SuccessDataResult;
import tr.shadowise_api.entity.Summary;
import tr.shadowise_api.repository.SummaryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;

    public IDataResult<Summary> saveSummary(Summary summary) {
        try {
            // Set creation and update times
            if (summary.getCreatedAt() == null) {
                summary.setCreatedAt(LocalDateTime.now());
            }
            summary.setUpdatedAt(LocalDateTime.now());
            
            Summary savedSummary = summaryRepository.save(summary);
            return new SuccessDataResult<>(savedSummary, "Summary saved successfully");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Failed to save summary: " + e.getMessage());
        }
    }

    public IDataResult<Summary> getSummaryByFileId(String fileId) {
        Optional<Summary> summary = summaryRepository.findByFileId(fileId);
        if (summary.isPresent()) {
            return new SuccessDataResult<>(summary.get(), "Summary retrieved successfully");
        }
        return new ErrorDataResult<>(null, "Summary not found for file ID: " + fileId);
    }
    
    public IDataResult<Summary> getSummaryById(String id) {
        Optional<Summary> summary = summaryRepository.findById(id);
        if (summary.isPresent()) {
            return new SuccessDataResult<>(summary.get(), "Summary retrieved successfully");
        }
        return new ErrorDataResult<>(null, "Summary not found with ID: " + id);
    }

    public IDataResult<List<Summary>> getSummariesByProjectId(String projectId) {
        List<Summary> summaries = summaryRepository.findByProjectId(projectId);
        if (!summaries.isEmpty()) {
            return new SuccessDataResult<>(summaries, "Summaries retrieved successfully");
        }
        return new ErrorDataResult<>(null, "No summaries found for project ID: " + projectId);
    }
    
    public IDataResult<Summary> getSummaryByFileIdAndParameters(String fileId, Integer maxWords, Double temperature) {
        Optional<Summary> summary = summaryRepository.findByFileIdAndMaxWordsAndTemperature(fileId, maxWords, temperature);
        if (summary.isPresent()) {
            return new SuccessDataResult<>(summary.get(), "Summary retrieved successfully");
        }
        return new ErrorDataResult<>(null, "Summary not found with the given parameters");
    }
    
    public IDataResult<Summary> getSummaryByProjectAndParameters(String projectId, String fileId, Integer maxWords, Double temperature) {
        Optional<Summary> summary = summaryRepository.findByProjectIdAndFileIdAndMaxWordsAndTemperature(projectId, fileId, maxWords, temperature);
        if (summary.isPresent()) {
            return new SuccessDataResult<>(summary.get(), "Summary retrieved successfully");
        }
        return new ErrorDataResult<>(null, "Summary not found with the given parameters");
    }
    
    public IDataResult<List<Summary>> getSummariesByProjectIdAndUserId(String projectId, String userId) {
        List<Summary> summaries = summaryRepository.findByProjectId(projectId);
        if (!summaries.isEmpty()) {
            return new SuccessDataResult<>(summaries, "Summaries retrieved successfully for project: " + projectId);
        }
        return new ErrorDataResult<>(null, "No summaries found for project ID: " + projectId);
    }
}