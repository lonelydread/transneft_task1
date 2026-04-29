package ru.singularity.task1.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NetworkNode {
    private String id;           // "1", "A", "VII"
    private NodeType type;       // SOURCE, CONSUMER, JUNCTION, INTERMEDIATE
    private double x, y;         // координаты

    public enum NodeType { SOURCE, CONSUMER, JUNCTION, INTERMEDIATE }


}

