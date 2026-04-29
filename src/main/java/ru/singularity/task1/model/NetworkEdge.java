package ru.singularity.task1.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NetworkEdge {
    private String id;
    private String fromNodeId;
    private String toNodeId;
    private double capacity; // пропускная способность
    private double flow;

}

