package ru.singularity.task1.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
public class OptimizationRequest {
    private List<NetworkNode> nodes;
    private List<NetworkEdge> edges;
    private String algorithm;    // "max_flow" | "min_cost" | "ml_forecast"
    private Map<String, Object> params;

    public List<NetworkNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<NetworkNode> nodes) {
        this.nodes = nodes;
    }

    public List<NetworkEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<NetworkEdge> edges) {
        this.edges = edges;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

