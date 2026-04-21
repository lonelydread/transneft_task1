package ru.singularity.task1.model;

public class NetworkNode {
    private String id;           // "1", "A", "VII"
    private NodeType type;       // SOURCE, CONSUMER, JUNCTION, INTERMEDIATE
    private double x, y;         // координаты
    private double capacity;     // МВт — для источников
    private double demand;       // МВт — для потребителей
    private double currentFlow;  // результат оптимизации

    public enum NodeType { SOURCE, CONSUMER, JUNCTION, INTERMEDIATE }

    public NetworkNode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }

    public double getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(double currentFlow) {
        this.currentFlow = currentFlow;
    }
}

