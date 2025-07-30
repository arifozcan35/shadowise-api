package tr.shadowise_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.shadowise_api.core.response.IDataResult;
import tr.shadowise_api.core.response.IResult;
import tr.shadowise_api.service.PythonApiService;

import java.util.Map;

@RestController
@RequestMapping("/api/python")
@RequiredArgsConstructor
public class PythonApiController {

    private final PythonApiService pythonApiService;

    @GetMapping
    public ResponseEntity<IDataResult<Map<String, Object>>> getResources() {
        return ResponseEntity.ok(pythonApiService.getResource());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IDataResult<Map<String, Object>>> getResourceById(@PathVariable String id) {
        return ResponseEntity.ok(pythonApiService.getResourceById(id));
    }

    @PostMapping
    public ResponseEntity<IDataResult<Map<String, Object>>> createResource(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(pythonApiService.createResource(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IDataResult<Map<String, Object>>> updateResource(
            @PathVariable String id, 
            @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(pythonApiService.updateResource(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IResult> deleteResource(@PathVariable String id) {
        return ResponseEntity.ok(pythonApiService.deleteResource(id));
    }
}