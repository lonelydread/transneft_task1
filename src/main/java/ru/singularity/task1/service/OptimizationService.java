package ru.singularity.task1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.singularity.task1.model.OptimizationRequest;
import ru.singularity.task1.model.OptimizationResult;

import java.util.ArrayList;

// service/OptimizationService.java
@Service
public class OptimizationService {

    private final RestTemplate   restTemplate;
    private final NetworkService networkService;

    public OptimizationService(RestTemplate restTemplate, NetworkService networkService) {
        this.restTemplate = restTemplate;
        this.networkService = networkService;
    }

    @Value("${python.service.url:http://localhost:8000}")
    private String pythonUrl;

    public OptimizationResult optimize(String algorithm) {
        OptimizationRequest request = new OptimizationRequest();
        request.setNodes(new ArrayList<>(networkService.getNodes().values()));
        request.setEdges(new ArrayList<>(networkService.getEdges().values()));

        OptimizationResult result = restTemplate.postForObject(
                pythonUrl + "/api/optimize",
                request,
                OptimizationResult.class
        );

        return result;
    }
}

