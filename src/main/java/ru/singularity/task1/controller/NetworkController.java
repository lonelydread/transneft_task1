package ru.singularity.task1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.singularity.task1.service.NetworkService;
import ru.singularity.task1.ui.panel.NetworkPanel;

import java.util.Map;

@RestController
@RequestMapping("/api/network")
public class NetworkController {

    private final NetworkService networkService;
    private final NetworkPanel networkPanel;

    public NetworkController(
            NetworkService networkService,
            NetworkPanel networkPanel
    ) {
        this.networkService = networkService;
        this.networkPanel = networkPanel;
    }

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
        networkPanel.refresh();
        return getNetwork();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearNetwork() {
        networkService.clearNetwork();
        networkPanel.refresh();
        return ResponseEntity.noContent().build();
    }
}

