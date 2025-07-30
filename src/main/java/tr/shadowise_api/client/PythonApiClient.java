package tr.shadowise_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(
    name = "python-api", 
    url = "${python.api.url}",
    configuration = tr.shadowise_api.config.FeignClientConfig.class
)
public interface PythonApiClient {

    @GetMapping("/api/resource")
    Map<String, Object> getResource();
    
    @PostMapping("/api/resource")
    Map<String, Object> createResource(@RequestBody Map<String, Object> payload);
    
    @GetMapping("/api/resource/{id}")
    Map<String, Object> getResourceById(@PathVariable("id") String id);
    
    @PutMapping("/api/resource/{id}")
    Map<String, Object> updateResource(
        @PathVariable("id") String id, 
        @RequestBody Map<String, Object> payload
    );
    
    @DeleteMapping("/api/resource/{id}")
    void deleteResource(@PathVariable("id") String id);
}