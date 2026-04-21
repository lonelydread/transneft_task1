package ru.singularity.task1.model;

public class NetworkNode {
    private String id;           // "1", "A", "VII"
    private NodeType type;       // SOURCE, CONSUMER, JUNCTION, INTERMEDIATE
    private double x, y;         // координаты

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

}

