package ru.singularity.task1.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.singularity.task1.model.OptimizationResult;
import ru.singularity.task1.service.OptimizationService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OptimizationController {

    private final OptimizationService optimizationService;

    @PostMapping("/optimize")
    public ResponseEntity<OptimizationResult> optimize() {
        return ResponseEntity.ok(optimizationService.optimize());
    }

}

