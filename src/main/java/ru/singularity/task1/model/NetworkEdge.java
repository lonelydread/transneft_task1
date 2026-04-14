package ru.singularity.task1.model;

public class NetworkEdge {
    private String id;
    private String fromNodeId;
    private String toNodeId;
    private double capacity;     // пропускная способность
    private double flow;         // оптимальный поток (из Python)
    private EdgeType type;       // FLOW_DIRECTION, FLOW_INPUT

    public enum EdgeType { FLOW_DIRECTION, FLOW_INPUT }

    public NetworkEdge() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public String getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(String toNodeId) {
        this.toNodeId = toNodeId;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }

    public EdgeType getType() {
        return type;
    }

    public void setType(EdgeType type) {
        this.type = type;
    }
}

