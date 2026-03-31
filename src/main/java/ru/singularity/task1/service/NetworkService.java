package ru.singularity.task1.service;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import ru.singularity.task1.model.NetworkEdge;
import ru.singularity.task1.model.NetworkNode;
import ru.singularity.task1.model.OptimizationResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// service/NetworkService.java
@Service
public class NetworkService {

    private final Map<String, NetworkNode> nodes = new ConcurrentHashMap<>();
    private final Map<String, NetworkEdge> edges = new ConcurrentHashMap<>();

    // JGraphT граф — только для алгоритмов на Java-стороне
    private DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph =
            new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    public NetworkNode addNode(NetworkNode node) {
        nodes.put(node.getId(), node);
        graph.addVertex(node.getId());
        return node;
    }

    public NetworkEdge addEdge(NetworkEdge edge) {
        edges.put(edge.getId(), edge);
        graph.addVertex(edge.getFromNodeId());
        graph.addVertex(edge.getToNodeId());
        DefaultWeightedEdge e =
                graph.addEdge(edge.getFromNodeId(), edge.getToNodeId());
        if (e != null) graph.setEdgeWeight(e, edge.getCapacity());
        return edge;
    }

    public NetworkNode getNode(String id) { return nodes.get(id); }
    public Map<String, NetworkNode> getNodes() { return nodes; }
    public Map<String, NetworkEdge> getEdges() { return edges; }

    public void applyResult(OptimizationResult result) {
        result.getEdgeFlows().forEach((id, flow) -> {
            NetworkEdge edge = edges.get(id);
            if (edge != null) edge.setFlow(flow);
        });
        result.getNodePredictions().forEach((id, val) -> {
            NetworkNode node = nodes.get(id);
            if (node != null) node.setCurrentFlow(val);
        });
    }

    public void clearNetwork() {
        nodes.clear();
        edges.clear();
        graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }
}

