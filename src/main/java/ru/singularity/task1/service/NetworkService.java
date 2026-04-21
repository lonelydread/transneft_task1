package ru.singularity.task1.service;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import ru.singularity.task1.model.NetworkEdge;
import ru.singularity.task1.model.NetworkNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class NetworkService {

    private final Map<String, NetworkNode> nodes = new ConcurrentHashMap<>();
    private final Map<String, NetworkEdge> edges = new ConcurrentHashMap<>();
    private final Random random = new Random();

    private DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph =
            new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    // -----------------------------------------------------------------------
    // CRUD
    // -----------------------------------------------------------------------

    public NetworkNode addNode(NetworkNode node) {
        nodes.put(node.getId(), node);
        graph.addVertex(node.getId());
        return node;
    }

    public NetworkEdge addEdge(NetworkEdge edge) {
        edges.put(edge.getId(), edge);
        graph.addVertex(edge.getFromNodeId());
        graph.addVertex(edge.getToNodeId());
        DefaultWeightedEdge e = graph.addEdge(edge.getFromNodeId(), edge.getToNodeId());
        if (e != null) graph.setEdgeWeight(e, edge.getCapacity());
        return edge;
    }

    public NetworkNode getNode(String id)              { return nodes.get(id); }
    public Map<String, NetworkNode> getNodes()         { return nodes; }
    public Map<String, NetworkEdge> getEdges()         { return edges; }

    public void clearNetwork() {
        nodes.clear();
        edges.clear();
        graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    // -----------------------------------------------------------------------
    // Demo network — точное воспроизведение «Схемы потоков» (Схема 1)
    //
    // Система координат: начало — левый верхний угол схемы.
    // Единица — условный пиксель, пропорции сохранены относительно PDF.
    //
    // Типы узлов:
    //   SOURCE   — источник потока  (▐▌)
    //   CONSUMER — потребитель      (⊗●)
    //   JUNCTION — узел соединения  (I. … XVIII.)
    // -----------------------------------------------------------------------
    public void createDemoNetwork() {
        clearNetwork();

        // ── Верхний кластер ──────────────────────────────────────────────

        // Источники
        n("N",  SOURCE,  0, 290);
        n("O",  SOURCE, 90, 225);
        n("L",  SOURCE, 640, 90);
        n("K",  SOURCE, 620, 195);
        n("J",  SOURCE, 270, 190);
        n("F",  SOURCE, 940, 195);
        n("G",  SOURCE, 1050, 215);

        // Потребители верхней части
        n("1",  CONSUMER, 510,  25);
        n("2",  CONSUMER, 610,  25);
        n("38", CONSUMER, 565,  100);
        n("15", CONSUMER, 215, 230);
        n("16", CONSUMER, 215, 130);
        n("35", CONSUMER, 330, 330);
        n("36", CONSUMER, 285, 265);
        n("27", CONSUMER, 150, 355);
        n("37", CONSUMER, 380, 120);
        n("32", CONSUMER, 460, 235);
        n("30", CONSUMER, 710, 230);
        n("31", CONSUMER, 790, 190);
        n("33", CONSUMER, 565, 300);
        n("34", CONSUMER, 590, 360);
        n("19", CONSUMER, 195, 400);
        n("20", CONSUMER, 195, 460);

        // Узлы соединения (верхняя часть)
        n("XVI.",  JUNCTION, 525, 195);
        n("IV.",   JUNCTION, 580, 230);
        n("XVII.", JUNCTION, 510, 270);
        n("III.",  JUNCTION, 670, 265);
        n("I.",    JUNCTION, 800, 265);
        n("II.",   JUNCTION, 960, 275);
        n("VIII.", JUNCTION, 380, 200);
        n("V.",    JUNCTION, 380, 265);
        n("VI.",   JUNCTION, 230, 310);
        n("VII.",  JUNCTION, 105, 300);

        // Промежуточные узлы (верхняя часть)
        n("XVI_L",  INTERMEDIATE, 582, 142);
        n("XVI_38", INTERMEDIATE, 545, 147);
        n("V_32",   INTERMEDIATE, 420, 250);
        n("III_33", INTERMEDIATE, 617, 282);
        n("III_34", INTERMEDIATE, 630, 312);
        n("I_30",   INTERMEDIATE, 755, 247);
        n("I_31",   INTERMEDIATE, 795, 227);
        n("VI_35",  INTERMEDIATE, 280, 320);
        n("VI_36",  INTERMEDIATE, 257, 287);

        // ── Нижний кластер ───────────────────────────────────────────────

        // Источники нижней части
        n("A",  SOURCE, 1065, 490);
        n("P",  SOURCE,  955, 490);
        n("M",  SOURCE,  750, 485);
        n("B",  SOURCE,  870, 630);
        n("C",  SOURCE,  765, 630);
        n("D",  SOURCE,  620, 480);
        n("E",  SOURCE,  550, 480);
        n("H",  SOURCE,  645, 345);  // источник, входящий в III.
        n("I",  SOURCE,  390, 335);  // источник (буква I), входящий в V.

        // Потребители нижней части
        n("6",  CONSUMER, 1100, 545);
        n("5",  CONSUMER,  1050, 620);
        n("7",  CONSUMER, 1100, 630);
        n("4",  CONSUMER,  1000, 660);
        n("9",  CONSUMER,  955, 620);
        n("11", CONSUMER,  900, 470);
        n("10", CONSUMER,  870, 510);
        n("12", CONSUMER,  830, 490);
        n("8",  CONSUMER,  680, 610);
        n("29", CONSUMER,  575, 610);
        n("26", CONSUMER,  480, 480);
        n("23", CONSUMER,  500, 640);
        n("22", CONSUMER,  460, 620);
        n("21", CONSUMER,  415, 500);
        n("28", CONSUMER,  400, 600);
        n("3",  CONSUMER,  360, 480);
        n("17", CONSUMER,  205, 505);
        n("18", CONSUMER,  170, 540);
        n("13", CONSUMER,  150, 600);
        n("14", CONSUMER,  160, 670);
        n("25", CONSUMER,  70, 570);
        n("24", CONSUMER,  255, 655);

        // Узлы соединения (нижняя часть)
        n("XVIII.", JUNCTION, 1040, 545);
        n("XI.",    JUNCTION,  965, 545);
        n("X.",     JUNCTION,  785, 545);
        n("IX.",    JUNCTION,  690, 545);
        n("XV.",    JUNCTION,  605, 545);
        n("XIV.",   JUNCTION,  530, 545);
        n("XII.",   JUNCTION,  345, 545);
        n("XIII.",  JUNCTION,  245, 575);

        // Промежуточные узлы (нижняя часть)
        n("XI_11",  INTERMEDIATE,  932, 507);
        n("XI_10",  INTERMEDIATE,  917, 527);
        n("IX_8",   INTERMEDIATE,  685, 577);
        n("XIV_23", INTERMEDIATE,  515, 592);
        n("XIV_26", INTERMEDIATE,  505, 512);
        n("XIV_22", INTERMEDIATE,  495, 582);
        n("XIV_21", INTERMEDIATE,  472, 522);
        n("XIV_28", INTERMEDIATE,  465, 572);
        n("XII_20", INTERMEDIATE,  270, 502);
        n("XII_19", INTERMEDIATE,  270, 472);
        n("XVIII_5",INTERMEDIATE, 1045, 582);
        n("XVIII_4",INTERMEDIATE, 1020, 602);
        n("XVIII_9",INTERMEDIATE,  997, 582);

        // ── Рёбра ────────────────────────────────────────────────────────

        List<String[]> links = new ArrayList<>();

        // ── Верхний кластер ──────────────────────────────────────────────
        links.add(e("1",     "2"));
        links.add(e("XVI_38",  "1"));
        links.add(e("XVI_38",  "38"));
        links.add(e("L",     "XVI_L"));
        links.add(e("XVI_L", "XVI_38"));
        links.add(e("XVI.", "XVI_L"));
        links.add(e("K",     "XVI."));
        links.add(e("IV.",   "XVI."));
        links.add(e("H",  "III."));
        links.add(e("III.",  "III_34"));
        links.add(e("III_34","III_33"));
        links.add(e("III_34","34"));
        links.add(e("III_33",  "33"));
        links.add(e("III_33",  "IV."));
        links.add(e("I_30",  "III."));
        links.add(e("I_30",  "30"));
        links.add(e("I_31",  "I_30"));
        links.add(e("I_31",  "31"));
        links.add(e("I.",  "I_31"));
        links.add(e("II.",  "I."));
        links.add(e("F",  "II."));
        links.add(e("G",  "II."));
        links.add(e("XVII.", "IV."));
        links.add(e("V_32", "XVII."));
        links.add(e("V_32", "32"));
        links.add(e("V.", "V_32"));
        links.add(e("I", "V."));
        links.add(e("V.", "VIII."));
        links.add(e("J", "VIII."));
        links.add(e("VIII.", "37"));
        links.add(e("V.", "VI_36"));
        links.add(e("VI_36", "36"));
        links.add(e("VI_35", "VI_36"));
        links.add(e("VI_35", "35"));
        links.add(e("VI.", "VI_35"));
        links.add(e("VI.", "15"));
        links.add(e("15", "16"));
        links.add(e("VII.", "VI."));
        links.add(e("O", "VII."));
        links.add(e("N", "VII."));
        links.add(e("VI.", "27"));
        links.add(e("XII_19", "VI."));
        links.add(e("XII_19", "19"));
        links.add(e("XII_20", "XII_19"));
        links.add(e("XII_20", "20"));
        links.add(e("XII.", "XII_20"));
        links.add(e("XII.", "3"));
        links.add(e("XII.", "XIII."));
        links.add(e("XIII.", "17"));
        links.add(e("XIII.", "18"));
        links.add(e("XIII.", "13"));
        links.add(e("XIII.", "24"));
        links.add(e("13", "25"));
        links.add(e("13", "14"));
        links.add(e("XIV_28", "XII."));
        links.add(e("XIV_28", "28"));
        links.add(e("XIV_21", "XIV_28"));
        links.add(e("XIV_21", "21"));
        links.add(e("XIV_22", "XIV_21"));
        links.add(e("XIV_22", "22"));
        links.add(e("XIV_26", "XIV_22"));
        links.add(e("XIV_26", "26"));
        links.add(e("XIV_23", "XIV_26"));
        links.add(e("XIV_23", "23"));
        links.add(e("XIV.", "XIV_23"));
        links.add(e("E", "XIV."));
        links.add(e("XIV.", "29"));
        links.add(e("XV.", "XIV."));
        links.add(e("D", "XV."));
        links.add(e("IX_8", "XV."));
        links.add(e("IX_8", "8"));
        links.add(e("IX.", "IX_8"));
        links.add(e("IX.", "I."));
        links.add(e("X.", "IX."));
        links.add(e("M", "X."));
        links.add(e("X.", "12"));
        links.add(e("B", "X."));
        links.add(e("C", "X."));
        links.add(e("XI_10", "X."));
        links.add(e("XI_10", "10"));
        links.add(e("XI_11", "XI_10"));
        links.add(e("XI_11", "11"));
        links.add(e("XI.", "XI_11"));
        links.add(e("P", "XI."));
        links.add(e("XVIII_9", "XI."));
        links.add(e("XVIII_9", "9"));
        links.add(e("XVIII_4", "XVIII_9"));
        links.add(e("XVIII_4", "4"));
        links.add(e("XVIII.", "XVIII_4"));
        links.add(e("A", "XVIII."));
        links.add(e("XVIII.", "XVIII_5"));
        links.add(e("XVIII_5", "5"));
        links.add(e("XVIII_5", "6"));
        links.add(e("6", "7"));

        // Добавляем все рёбра
        int idx = 1;
        for (String[] link : links) {
            addEdge(createEdge("E-" + idx++, link[0], link[1], 100));
        }
    }

    // -----------------------------------------------------------------------
    // Вспомогательные методы
    // -----------------------------------------------------------------------

    private static final NetworkNode.NodeType SOURCE   = NetworkNode.NodeType.SOURCE;
    private static final NetworkNode.NodeType CONSUMER = NetworkNode.NodeType.CONSUMER;
    private static final NetworkNode.NodeType JUNCTION = NetworkNode.NodeType.JUNCTION;
    private static final NetworkNode.NodeType INTERMEDIATE = NetworkNode.NodeType.INTERMEDIATE;

    /** Сокращённый вызов addNode */
    private void n(String id, NetworkNode.NodeType type, double x, double y) {
        addNode(createNode(id, type, x, y));
    }

    /** Сокращённое создание пары [from, to] */
    private String[] e(String from, String to) {
        return new String[]{from, to};
    }

    private NetworkNode createNode(String id, NetworkNode.NodeType type, double x, double y) {
        NetworkNode node = new NetworkNode();
        node.setId(id);
        node.setType(type);
        node.setX(x);
        node.setY(y);
        if (type == SOURCE) {
            node.setCapacity(120);
        } else if (type == CONSUMER) {
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

    /**
     * Генерирует демонстрационную "заявку" на текущей схеме:
     * - выбирает часть потребителей и назначает им случайный demand;
     * - заполняет flow у рёбер в рамках их capacity;
     * - обновляет capacity источников исходя из общей потребности.
     */
    public Map<String, Object> applyRandomRequestDemo() {
        if (nodes.isEmpty() || edges.isEmpty()) {
            createDemoNetwork();
        }

        int selectedConsumers = 0;
        double totalDemand = 0.0;

        for (NetworkNode node : nodes.values()) {
            node.setCurrentFlow(0.0);
            if (node.getType() == CONSUMER) {
                if (random.nextDouble() < 0.35) {
                    double demand = 20_000 + random.nextInt(280_001);
                    node.setDemand(demand);
                    node.setCurrentFlow(demand);
                    selectedConsumers++;
                    totalDemand += demand;
                } else {
                    node.setDemand(0.0);
                }
            }
        }

        int sourceCount = 0;
        for (NetworkNode node : nodes.values()) {
            if (node.getType() == SOURCE) {
                sourceCount++;
            }
        }
        double avgPerSource = sourceCount > 0 ? totalDemand / sourceCount : 0.0;
        for (NetworkNode node : nodes.values()) {
            if (node.getType() == SOURCE) {
                double jitter = avgPerSource * (0.75 + random.nextDouble() * 0.5);
                node.setCapacity(Math.max(10_000, jitter));
            }
        }

        for (NetworkEdge edge : edges.values()) {
            double cap = Math.max(edge.getCapacity(), 1.0);
            double flow = cap * (0.2 + random.nextDouble() * 0.7);
            edge.setFlow(flow);
        }

        Map<String, Object> meta = new HashMap<>();
        meta.put("selectedConsumers", selectedConsumers);
        meta.put("totalDemand", totalDemand);
        meta.put("updatedEdges", edges.size());
        return meta;
    }
}