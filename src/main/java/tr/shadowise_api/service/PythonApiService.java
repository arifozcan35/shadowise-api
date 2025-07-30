package tr.shadowise_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.shadowise_api.client.PythonApiClient;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.IResult;
import tr.shadowise_api.core.response.SuccessDataResult;
import tr.shadowise_api.core.response.SuccessResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PythonApiService {

    private final PythonApiClient pythonApiClient;

    public IDataResult<Map<String, Object>> getResource() {
        Map<String, Object> result = pythonApiClient.getResource();
        return new SuccessDataResult<>(result, "Resource data fetched successfully");
    }

    public IDataResult<Map<String, Object>> getResourceById(String id) {
        Map<String, Object> result = pythonApiClient.getResourceById(id);
        return new SuccessDataResult<>(result, "Resource data fetched successfully");
    }

    public IDataResult<Map<String, Object>> createResource(Map<String, Object> payload) {
        Map<String, Object> result = pythonApiClient.createResource(payload);
        return new SuccessDataResult<>(result, "Resource created successfully");
    }

    public IDataResult<Map<String, Object>> updateResource(String id, Map<String, Object> payload) {
        Map<String, Object> result = pythonApiClient.updateResource(id, payload);
        return new SuccessDataResult<>(result, "Resource updated successfully");
    }

    public IResult deleteResource(String id) {
        pythonApiClient.deleteResource(id);
        return new SuccessResult("Resource deleted successfully");
    }
}