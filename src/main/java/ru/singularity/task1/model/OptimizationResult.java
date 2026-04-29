package ru.singularity.task1.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Data
@Setter
@Getter
public class OptimizationResult {
    private double r;
    private Map<String, Double> flows;
    private ArrayList<String> delivered;
}
