package ru.singularity.task1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.singularity.task1.model.OptimizationResult;

@Service
@RequiredArgsConstructor
public class OptimizationService {

    private final RestTemplate restTemplate;

    @Value("${python.service.url}")
    private String pythonUrl;

    public OptimizationResult optimize() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("source", new ClassPathResource("static/source.csv"));
        body.add("graph", new ClassPathResource("static/graph.csv"));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(pythonUrl + "/api/optimize", request, OptimizationResult.class);
    }
}
