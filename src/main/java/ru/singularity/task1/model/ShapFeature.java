package ru.singularity.task1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShapFeature {
    @JsonProperty("name")
    private String name;
    @JsonProperty("importance")
    private double importance;
    @JsonProperty("rank")
    private int rank;
}
