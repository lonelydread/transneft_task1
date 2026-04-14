package ru.singularity.task1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import ru.singularity.task1.model.OptimizationRequest;
//import ru.singularity.task1.model.OptimizationResult;

import java.util.ArrayList;

// service/OptimizationService.java
@Service
@RequiredArgsConstructor
public class OptimizationService {

    private final RestTemplate   restTemplate;
    private final NetworkService networkService;

    @Value("${python.service.url:http://localhost:8000}")
    private String pythonUrl;

    public OptimizationResult optimize(String algorithm) {
        OptimizationRequest request = OptimizationRequest.builder()
                .nodes(new ArrayList<>(networkService.getNodes().values()))
                .edges(new ArrayList<>(networkService.getEdges().values()))
                .algorithm(algorithm)
                .build();

        OptimizationResult result = restTemplate.postForObject(
                pythonUrl + "/api/optimize",
                request,
                OptimizationResult.class
        );

        // Применяем результат прямо в памяти
        networkService.applyResult(result);
        return result;
    }
}

