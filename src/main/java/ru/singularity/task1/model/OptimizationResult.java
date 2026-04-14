package ru.singularity.task1.model;

import lombok.Data;

import java.util.Map;

@Data
public class OptimizationResult {
    private double maxFlow;
    private double minCost;
    private Map<String, Double> edgeFlows;   // edgeId -> flow value
    private Map<String, Double> nodePredictions; // nodeId -> forecast
    private String status;
    private long computationTimeMs;

    public double getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(double maxFlow) {
        this.maxFlow = maxFlow;
    }

    public double getMinCost() {
        return minCost;
    }

    public void setMinCost(double minCost) {
        this.minCost = minCost;
    }

    public Map<String, Double> getEdgeFlows() {
        return edgeFlows;
    }

    public void setEdgeFlows(Map<String, Double> edgeFlows) {
        this.edgeFlows = edgeFlows;
    }

    public Map<String, Double> getNodePredictions() {
        return nodePredictions;
    }

    public void setNodePredictions(Map<String, Double> nodePredictions) {
        this.nodePredictions = nodePredictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getComputationTimeMs() {
        return computationTimeMs;
    }

    public void setComputationTimeMs(long computationTimeMs) {
        this.computationTimeMs = computationTimeMs;
    }
}
