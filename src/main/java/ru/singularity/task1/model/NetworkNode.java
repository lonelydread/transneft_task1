package ru.singularity.task1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkNode {
    private String id;           // "1", "A", "VII"
    private NodeType type;       // SOURCE, CONSUMER, JUNCTION
    private double x, y;         // координаты для Swing
    private double capacity;     // МВт — для источников
    private double demand;       // МВт — для потребителей
    private double currentFlow;  // результат оптимизации

    public enum NodeType { SOURCE, CONSUMER, JUNCTION }
}

