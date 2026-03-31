package ru.singularity.task1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.singularity.task1.model.NetworkEdge;
import ru.singularity.task1.model.NetworkNode;
import ru.singularity.task1.model.OptimizationResult;
import ru.singularity.task1.service.NetworkService;
import ru.singularity.task1.service.OptimizationService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OptimizationController {

    private final NetworkService networkService;
    private final OptimizationService optimizationService;

    @PostMapping("/nodes")
    public ResponseEntity<NetworkNode> addNode(@RequestBody NetworkNode node) {
        return ResponseEntity.ok(networkService.addNode(node));
    }

    @PostMapping("/edges")
    public ResponseEntity<NetworkEdge> addEdge(@RequestBody NetworkEdge edge) {
        return ResponseEntity.ok(networkService.addEdge(edge));
    }

    @GetMapping("/network")
    public ResponseEntity<Map<String, Object>> getNetwork() {
        return ResponseEntity.ok(Map.of(
                "nodes", networkService.getNodes().values(),
                "edges", networkService.getEdges().values()
        ));
    }

    @PostMapping("/optimize/{algorithm}")
    public ResponseEntity<OptimizationResult> optimize(
            @PathVariable String algorithm) {
        return ResponseEntity.ok(optimizationService.optimize(algorithm));
    }

    @DeleteMapping("/network")
    public ResponseEntity<Void> clearNetwork() {
        networkService.clearNetwork();
        return ResponseEntity.noContent().build();
    }
}

