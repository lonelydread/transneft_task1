package ru.singularity.task1.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import ru.singularity.task1.model.OptimizationResult;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OptimizationController {

    private final RestTemplate restTemplate;

    @Value("${python.service.url}")
    private String pythonUrl;

    @PostMapping("/optimize")
    public ResponseEntity<OptimizationResult> optimize() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("source", new ClassPathResource("static/source.csv"));
        body.add("graph", new ClassPathResource("static/graph.csv"));
        

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        OptimizationResult result = restTemplate.postForObject(pythonUrl + "/api/optimize", request, OptimizationResult.class);
        return ResponseEntity.ok(result);
    }

}

