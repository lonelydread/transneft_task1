package ru.singularity.task1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkEdge {
    private String id;
    private String fromNodeId;
    private String toNodeId;
    private double capacity;     // пропускная способность
    private double flow;         // оптимальный поток (из Python)
    private EdgeType type;       // FLOW_DIRECTION, FLOW_INPUT

    public enum EdgeType { FLOW_DIRECTION, FLOW_INPUT }
}

