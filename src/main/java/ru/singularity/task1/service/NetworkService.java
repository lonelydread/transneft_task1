package ru.singularity.task1.service;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.singularity.task1.model.NetworkEdge;
import ru.singularity.task1.model.NetworkNode;
import ru.singularity.task1.model.OptimizationResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public void createDemoNetwork() {
        clearNetwork();
        List<String> sources = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P");
        List<String> junctions = Arrays.asList("I.", "II.", "III.", "IV.", "V.", "VI.",
                "VII.", "VIII.", "IX.", "X.", "XI.", "XII.", "XIII.", "XIV.", "XV.",
                "XVII.", "XVIII.");

        addNode(createNode("2", NetworkNode.NodeType.CONSUMER, 600, 20));
        addNode(createNode("1", NetworkNode.NodeType.CONSUMER, 500, 20));
        addNode(createNode("38", NetworkNode.NodeType.CONSUMER, 520, 50));
        addNode(createNode("L", NetworkNode.NodeType.SOURCE, 580, 100));
        addNode(createNode("K", NetworkNode.NodeType.SOURCE, 580, 180));
        addNode(createNode("XVI.", NetworkNode.NodeType.JUNCTION, 500, 180));
        addNode(createNode("IV.", NetworkNode.NodeType.JUNCTION, 600, 220));
        addNode(createNode("III.", NetworkNode.NodeType.JUNCTION, 680, 260));
        addNode(createNode("XVII.", NetworkNode.NodeType.JUNCTION, 520, 260));
        addNode(createNode("33", NetworkNode.NodeType.CONSUMER, 570, 265));
        addNode(createNode("34", NetworkNode.NodeType.CONSUMER, 630, 265));
        addNode(createNode("I.", NetworkNode.NodeType.JUNCTION, 800, 260));
        addNode(createNode("30", NetworkNode.NodeType.CONSUMER, 720, 230));
        addNode(createNode("31", NetworkNode.NodeType.CONSUMER, 750, 210));
        addNode(createNode("II.", NetworkNode.NodeType.JUNCTION, 1000, 260));
        addNode(createNode("F", NetworkNode.NodeType.SOURCE, 950, 200));
        addNode(createNode("G", NetworkNode.NodeType.SOURCE, 1050, 200));
        addNode(createNode("32", NetworkNode.NodeType.CONSUMER, 450, 245));
        addNode(createNode("V.", NetworkNode.NodeType.JUNCTION, 400, 260));
        addNode(createNode("VIII.", NetworkNode.NodeType.JUNCTION, 400, 200));
        addNode(createNode("37", NetworkNode.NodeType.CONSUMER, 400, 150));
        addNode(createNode("J", NetworkNode.NodeType.SOURCE, 350, 190));

        List<String[]> links = new ArrayList<>();
        // Sources to core graph
        links.add(new String[]{"N", "VII."});
        links.add(new String[]{"L", "38"});
        links.add(new String[]{"L", "2"});
        links.add(new String[]{"L", "XVI."});
        links.add(new String[]{"K", "XVI."});
        links.add(new String[]{"F", "II."});
        links.add(new String[]{"G", "II."});
        links.add(new String[]{"J", "VIII."});
        links.add(new String[]{"I", "V"});
        links.add(new String[]{"H", "III."});
        links.add(new String[]{"M", "X."});
        links.add(new String[]{"B", "X."});
        links.add(new String[]{"C", "IX."});
        links.add(new String[]{"D", "XV."});
        links.add(new String[]{"E", "XIV."});
        links.add(new String[]{"A", "XVIII."});
        links.add(new String[]{"P", "XI."});

        // Upper and middle parts
        links.add(new String[]{"38", "1"});
        links.add(new String[]{"38", "XVI."});
        links.add(new String[]{"XVI.", "IV."});
        links.add(new String[]{"IV.", "XVII."});
        links.add(new String[]{"IV.", "III."});
        links.add(new String[]{"XVII.", "III."});
        links.add(new String[]{"III.", "I."});
        links.add(new String[]{"I.", "II."});
        links.add(new String[]{"I.", "X."});
        links.add(new String[]{"I.", "30"});
        links.add(new String[]{"I.", "31"});
        links.add(new String[]{"VIII.", "V."});
        links.add(new String[]{"VIII.", "XVII."});
        links.add(new String[]{"VIII.", "32"});
        links.add(new String[]{"VIII.", "37"});
        links.add(new String[]{"V.", "VI."});
        links.add(new String[]{"VI.", "VII."});
        links.add(new String[]{"VI.", "35"});
        links.add(new String[]{"VI.", "36"});
        links.add(new String[]{"VI.", "19"});
        links.add(new String[]{"VI.", "20"});
        links.add(new String[]{"VI.", "XIII."});
        links.add(new String[]{"VII.", "15"});
        links.add(new String[]{"VII.", "16"});
        links.add(new String[]{"VII.", "27"});

        // Lower part
        links.add(new String[]{"XVIII.", "XI."});
        links.add(new String[]{"XI.", "X."});
        links.add(new String[]{"X.", "IX."});
        links.add(new String[]{"IX.", "XV."});
        links.add(new String[]{"XV.", "XIV."});
        links.add(new String[]{"XIV.", "XII."});
        links.add(new String[]{"XII.", "XIII."});
        links.add(new String[]{"XII.", "21"});
        links.add(new String[]{"XII.", "22"});
        links.add(new String[]{"XII.", "28"});
        links.add(new String[]{"XIII.", "17"});
        links.add(new String[]{"XIII.", "18"});
        links.add(new String[]{"XIII.", "3"});
        links.add(new String[]{"XIII.", "25"});
        links.add(new String[]{"XIII.", "13"});
        links.add(new String[]{"XIII.", "14"});
        links.add(new String[]{"XIII.", "24"});
        links.add(new String[]{"XIV.", "26"});
        links.add(new String[]{"XIV.", "23"});
        links.add(new String[]{"XV.", "29"});
        links.add(new String[]{"IX.", "8"});
        links.add(new String[]{"X.", "10"});
        links.add(new String[]{"X.", "12"});
        links.add(new String[]{"XI.", "9"});
        links.add(new String[]{"XI.", "4"});
        links.add(new String[]{"XI.", "11"});
        links.add(new String[]{"XVIII.", "6"});
        links.add(new String[]{"XVIII.", "5"});
        links.add(new String[]{"XVIII.", "7"});

        int edgeNum = 1;
        for (String[] link : links) {
            addEdge(createEdge("E-" + edgeNum++, link[0], link[1], 100));
        }

    }

    private NetworkNode createNode(String id, NetworkNode.NodeType type, double x, double y) {
        NetworkNode node = new NetworkNode();
        node.setId(id);
        node.setType(type);
        node.setX(x);
        node.setY(y);
        if (type == NetworkNode.NodeType.SOURCE) {
            node.setCapacity(120);
        } else if (type == NetworkNode.NodeType.CONSUMER) {
            node.setDemand(30);
        }
        return node;
    }

    private NetworkEdge createEdge(String id, String from, String to, double capacity) {
        NetworkEdge edge = new NetworkEdge();
        edge.setId(id);
        edge.setFromNodeId(from);
        edge.setToNodeId(to);
        edge.setCapacity(capacity);
        edge.setType(NetworkEdge.EdgeType.FLOW_DIRECTION);
        return edge;
    }


    public void clearNetwork() {
        nodes.clear();
        edges.clear();
        graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }
}

