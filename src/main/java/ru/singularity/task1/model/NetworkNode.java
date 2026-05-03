package ru.singularity.task1.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NetworkNode {
    private String id;           
    private NodeType type;       
    private double x, y;        

    public enum NodeType { SOURCE, CONSUMER, JUNCTION, INTERMEDIATE }


}

