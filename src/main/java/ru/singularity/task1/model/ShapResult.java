package ru.singularity.task1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ShapResult {
    @JsonProperty("features")
    private List<ShapFeature> features;
    @JsonProperty("recommendations")
    private List<String> recommendations;
}
