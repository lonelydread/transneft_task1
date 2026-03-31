package ru.singularity.task1.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class OptimizationRequest {
    private List<NetworkNode> nodes;
    private List<NetworkEdge> edges;
    private String algorithm;    // "max_flow" | "min_cost" | "ml_forecast"
    private Map<String, Object> params;
}

