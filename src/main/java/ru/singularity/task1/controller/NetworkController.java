package ru.singularity.task1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.singularity.task1.service.NetworkService;

import java.util.Map;

@RestController
@RequestMapping("/api/network")
@RequiredArgsConstructor
public class NetworkController {

    private final NetworkService networkService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNetwork() {
        return ResponseEntity.ok(Map.of(
                "nodes", networkService.getNodes().values(),
                "edges", networkService.getEdges().values()
        ));
    }

    @PostMapping("/demo")
    public ResponseEntity<Map<String, Object>> createDemoNetwork() {
        networkService.createDemoNetwork();
        return getNetwork();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearNetwork() {
        networkService.clearNetwork();
        return ResponseEntity.noContent().build();
    }
}

