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
        n("N",  SOURCE,   40, 290);
        n("O",  SOURCE,  150, 210);
        n("L",  SOURCE,  620, 120);
        n("K",  SOURCE,  620, 170);
        n("J",  SOURCE,  340, 220);
        n("F",  SOURCE,  1070, 260);
        n("G",  SOURCE, 1140, 260);

        // Потребители верхней части
        n("1",  CONSUMER, 540, 10);
        n("2",  CONSUMER, 640, 10);
        n("38", CONSUMER, 590, 60);
        n("15", CONSUMER, 220, 260);
        n("16", CONSUMER, 260, 190);
        n("35", CONSUMER, 290, 290);
        n("36", CONSUMER, 350, 290);
        n("27", CONSUMER, 130, 370);
        n("37", CONSUMER, 410, 200);
        n("32", CONSUMER, 490, 290);
        n("30", CONSUMER, 800, 280);
        n("31", CONSUMER, 860, 280);
        n("33", CONSUMER, 630, 340);
        n("34", CONSUMER, 670, 390);
        n("19", CONSUMER, 120, 430);
        n("20", CONSUMER, 140, 490);

        // Узлы соединения (верхняя часть)
        n("XVI.",  JUNCTION, 540, 180);
        n("IV.",   JUNCTION, 650, 240);
        n("XVII.", JUNCTION, 550, 360);
        n("III.",  JUNCTION, 740, 350);
        n("I.",    JUNCTION, 920, 350);
        n("II.",   JUNCTION, 1080, 350);
        n("VIII.", JUNCTION, 410, 270);
        n("V.",    JUNCTION, 410, 360);
        n("VI.",   JUNCTION, 230, 350);
        n("VII.",  JUNCTION, 130, 320);

        // Промежуточные узлы (верхняя часть)
        n("XVI_L",  INTERMEDIATE, 550, 120);
        n("XVI_38", INTERMEDIATE, 550, 70);
        n("V_32",   INTERMEDIATE, 500, 370);
        n("III_33", INTERMEDIATE, 700, 290);
        n("III_34", INTERMEDIATE, 720, 320);
        n("I_30",   INTERMEDIATE, 810, 360);
        n("I_31",   INTERMEDIATE, 860, 360);
        n("VI_35",  INTERMEDIATE, 310, 370);
        n("VI_36",  INTERMEDIATE, 360, 370);

        // ── Нижний кластер ───────────────────────────────────────────────

        // Источники нижней части
        n("A",  SOURCE, 1120, 440);
        n("P",  SOURCE,  980, 480);
        n("M",  SOURCE,  850, 510);
        n("B",  SOURCE,  870, 660);
        n("C",  SOURCE,  800, 660);
        n("D",  SOURCE,  620, 500);
        n("E",  SOURCE,  550, 490);
        n("H",  SOURCE,  740, 420);  // источник, входящий в III.
        n("I",  SOURCE,  410, 420);  // источник (буква I), входящий в V.

        // Потребители нижней части
        n("6",  CONSUMER, 1220, 630);
        n("5",  CONSUMER, 1170, 590);
        n("7",  CONSUMER, 1220, 730);
        n("4",  CONSUMER, 1140, 680);
        n("9",  CONSUMER,  1050, 500);
        n("11", CONSUMER,  990, 670);
        n("10", CONSUMER,  920, 640);
        n("12", CONSUMER,  920, 500);
        n("8",  CONSUMER,  690, 500);
        n("29", CONSUMER,  610, 680);
        n("26", CONSUMER,  400, 500);
        n("23", CONSUMER,  450, 660);
        n("22", CONSUMER,  330, 640);
        n("21", CONSUMER,  340, 490);
        n("28", CONSUMER,  270, 520);
        n("3",  CONSUMER,  180, 530);
        n("17", CONSUMER,  170, 590);
        n("18", CONSUMER,  100, 610);
        n("13", CONSUMER,  80, 670);
        n("14", CONSUMER,  40, 750);
        n("25", CONSUMER,   10, 660);
        n("24", CONSUMER,  200, 750);

        // Узлы соединения (нижняя часть)
        n("XVIII.", JUNCTION, 1180, 510);
        n("XI.",    JUNCTION,  1020, 570);
        n("X.",     JUNCTION,  860, 570);
        n("IX.",    JUNCTION,  770, 570);
        n("XV.",    JUNCTION,  650, 570);
        n("XIV.",   JUNCTION,  570, 570);
        n("XII.",   JUNCTION,  250, 620);
        n("XIII.",  JUNCTION,  160, 670);

        // Промежуточные узлы (нижняя часть)
        n("XI_11",  INTERMEDIATE,  970, 580);
        n("XI_10",  INTERMEDIATE,  930, 580);
        n("IX_8",   INTERMEDIATE,  720, 580);
        n("XIV_23", INTERMEDIATE,  520, 580);
        n("XIV_26", INTERMEDIATE,  470, 580);
        n("XIV_22", INTERMEDIATE,  420, 580);
        n("XIV_21", INTERMEDIATE,  370, 580);
        n("XIV_28", INTERMEDIATE,  320, 580);
        n("XII_20", INTERMEDIATE,  240, 480);
        n("XII_19", INTERMEDIATE,  220, 420);
        n("XVIII_5",INTERMEDIATE, 1230, 560);
        n("XVIII_4",INTERMEDIATE, 1130, 600);
        n("XVIII_9",INTERMEDIATE, 1080, 590);

        // ── Рёбра ────────────────────────────────────────────────────────

        List<String[]> links = new ArrayList<>();

        // ── Верхний кластер ──────────────────────────────────────────────
        links.add(e("1",       "2",        27945.0));
        links.add(e("XVI_38",  "1",        27945.0));
        links.add(e("XVI_38",  "38",       27945.0));
        links.add(e("L",       "XVI_L",    99999999.0));
        links.add(e("XVI_L",   "XVI_38",   27945.0));
        links.add(e("XVI.",    "XVI_L",    27945.0));
        links.add(e("K",       "XVI.",     99999999.0));
        links.add(e("IV.",     "XVI.",     12672.0));
        links.add(e("H",       "III.",     99999999.0));
        links.add(e("III.",    "III_34",   7057.0));
        links.add(e("III_34",  "III_33",   7057.0));
        links.add(e("III_34",  "34",       7057.0));
        links.add(e("III_33",  "33",       7057.0));
        links.add(e("III_33",  "IV.",      7057.0));
        links.add(e("I_30",    "III.",     7057.0));
        links.add(e("I_30",    "30",       11578.0));
        links.add(e("I_31",    "I_30",     11578.0));
        links.add(e("I_31",    "31",       11578.0));
        links.add(e("I.",      "I_31",     11578.0));
        links.add(e("II.",     "I.",       7057.0));
        links.add(e("F",       "II.",      99999999.0));
        links.add(e("G",       "II.",      99999999.0));
        links.add(e("XVII.",   "IV.",      12672.0));
        links.add(e("V_32",    "XVII.",    7322.0));
        links.add(e("V_32",    "32",       7322.0));
        links.add(e("V.",      "V_32",     7322.0));
        links.add(e("I",       "V.",       99999999.0));
        links.add(e("V.",      "VIII.",    1614.0));
        links.add(e("J",       "VIII.",    3573.0));
        links.add(e("VIII.",   "37",       1614.0));
        links.add(e("V.",      "VI_36",    5034.0));
        links.add(e("VI_36",   "36",       5034.0));
        links.add(e("VI_35",   "VI_36",    5034.0));
        links.add(e("VI_35",   "35",       5034.0));
        links.add(e("VI.",     "VI_35",    5034.0));
        links.add(e("VI.",     "15",       14017.0));
        links.add(e("15",      "16",       14017.0));
        links.add(e("VII.",    "VI.",      3501.0));
        links.add(e("O",       "VII.",     1762.0));
        links.add(e("N",       "VII.",     2529.0));
        links.add(e("VI.",     "27",       14017.0));
        links.add(e("XII_19",  "VI.",      10516.0));
        links.add(e("XII_19",  "19",       10516.0));
        links.add(e("XII_20",  "XII_19",   10516.0));
        links.add(e("XII_20",  "20",       10516.0));
        links.add(e("XII.",    "XII_20",   10516.0));
        links.add(e("XII.",    "3",        14548.0));
        links.add(e("XII.",    "XIII.",    5087.0));
        links.add(e("XIII.",   "17",       1651.0));
        links.add(e("XIII.",   "18",       1651.0));
        links.add(e("XIII.",   "13",       1571.0));
        links.add(e("XIII.",   "24",       1864.0));
        links.add(e("13",      "25",       1381.0));
        links.add(e("13",      "14",       1571.0));
        links.add(e("XIV_28",  "XII.",     14548.0));
        links.add(e("XIV_28",  "28",       14548.0));
        links.add(e("XIV_21",  "XIV_28",   14548.0));
        links.add(e("XIV_21",  "21",       14548.0));
        links.add(e("XIV_22",  "XIV_21",   14548.0));
        links.add(e("XIV_22",  "22",       14548.0));
        links.add(e("XIV_26",  "XIV_22",   14548.0));
        links.add(e("XIV_26",  "26",       14548.0));
        links.add(e("XIV_23",  "XIV_26",   14548.0));
        links.add(e("XIV_23",  "23",       14548.0));
        links.add(e("XIV.",    "XIV_23",   14548.0));
        links.add(e("E",       "XIV.",     99999999.0));
        links.add(e("XIV.",    "29",       11089.0));
        links.add(e("XV.",     "XIV.",     11089.0));
        links.add(e("D",       "XV.",      99999999.0));
        links.add(e("IX_8",    "XV.",      8991.0));
        links.add(e("IX_8",    "8",        8991.0));
        links.add(e("IX.",     "IX_8",     8991.0));
        links.add(e("IX.",     "I.",       6269.0));
        links.add(e("X.",      "IX.",      12900.0));
        links.add(e("M",       "X.",       2612.0));
        links.add(e("X.",      "12",       5901.0));
        links.add(e("B",       "X.",       99999999.0));
        links.add(e("C",       "X.",       4503.0));
        links.add(e("XI_10",   "X.",       5901.0));
        links.add(e("XI_10",   "10",       5901.0));
        links.add(e("XI_11",   "XI_10",    5901.0));
        links.add(e("XI_11",   "11",       5901.0));
        links.add(e("XI.",     "XI_11",    5901.0));
        links.add(e("P",       "XI.",      2670.0));
        links.add(e("XVIII_9", "XI.",      3644.0));
        links.add(e("XVIII_9", "9",        3644.0));
        links.add(e("XVIII_4", "XVIII_9",  3644.0));
        links.add(e("XVIII_4", "4",        3644.0));
        links.add(e("XVIII.",  "XVIII_4",  3644.0));
        links.add(e("A",       "XVIII.",   99999999.0));
        links.add(e("XVIII.",  "XVIII_5",  4878.0));
        links.add(e("XVIII_5", "5",        4878.0));
        links.add(e("XVIII_5", "6",        4878.0));
        links.add(e("6",       "7",        4878.0));

        // Добавляем все рёбра
        int idx = 1;
        for (String[] link : links) {
            addEdge(createEdge("E-" + idx++, link[0], link[1], link[2]));
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


    private String[] e(String from, String to, double capacity) {
        return new String[]{from, to, String.valueOf(capacity)};
    }

    private NetworkNode createNode(String id, NetworkNode.NodeType type, double x, double y) {
        NetworkNode node = new NetworkNode();
        node.setId(id);
        node.setType(type);
        node.setX(x);
        node.setY(y);
        return node;
    }

    private NetworkEdge createEdge(String id, String from, String to, String capacity) {
        NetworkEdge edge = new NetworkEdge();
        edge.setId(id);
        edge.setFromNodeId(from);
        edge.setToNodeId(to);
        edge.setCapacity(Double.valueOf(capacity));
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

        for (NetworkEdge edge : edges.values()) {
            double cap = Math.max(edge.getCapacity(), 1.0);
            double flow = cap * (0.2 + random.nextDouble() * 0.7);
            edge.setFlow(flow);
        }

        Map<String, Object> meta = new HashMap<>();

        meta.put("updatedEdges", edges.size());
        return meta;
    }
}