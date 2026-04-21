package ru.singularity.task1.ui.panel;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import org.springframework.stereotype.Component;
import ru.singularity.task1.model.NetworkEdge;
import ru.singularity.task1.model.NetworkNode;
import ru.singularity.task1.service.NetworkService;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class NetworkPanel extends JPanel {

	private final NetworkService networkService;
	private final mxGraph graph;
	private final Object graphParent;
	private final mxGraphComponent graphComponent;
	private final Map<Object, String> cellToNodeId = new HashMap<>();

	public NetworkPanel(NetworkService networkService) {
		this.networkService = networkService;
		this.graph = new mxGraph() {
			@Override
			public boolean isCellEditable(Object cell) {
				return false;
			}
		};
		this.graph.setAllowDanglingEdges(false);
		this.graph.setCellsResizable(false);
		this.graph.setCellsDisconnectable(false);
		this.graphParent = graph.getDefaultParent();
		this.graphComponent = new mxGraphComponent(graph);
		this.graphComponent.setConnectable(false);
		this.graphComponent.getGraphControl().setBackground(Color.WHITE);

		setLayout(new BorderLayout());
		add(graphComponent, BorderLayout.CENTER);
		refresh();
	}

	public void printCoordinates() {
		Object[] vertices = graph.getChildVertices(graphParent);
		java.util.Arrays.stream(vertices)
				.filter(cellToNodeId::containsKey)
				.sorted(java.util.Comparator.comparing(v -> cellToNodeId.get(v)))
				.forEach(v -> {
					mxGeometry geo = graph.getCellGeometry(v);
					if (geo != null) {
						System.out.printf("%-12s (%.0f, %.0f)%n", cellToNodeId.get(v), geo.getX(), geo.getY());
					}
				});
	}

	public void refresh() {
		SwingUtilities.invokeLater(() -> {
			graph.getModel().beginUpdate();
			try {
				graph.removeCells(graph.getChildVertices(graphParent));
				graph.removeCells(graph.getChildEdges(graphParent));

				cellToNodeId.clear();
				Map<String, Object> vertices = new HashMap<>();
				for (NetworkNode node : networkService.getNodes().values()) {
					String style = nodeStyle(node);
					String label = nodeLabel(node);
					Object v = graph.insertVertex(
							graphParent,
							node.getId(),
							label,
							node.getX(),
							node.getY(),
							nodeWidth(node),
							nodeHeight(node),
							style
					);
					vertices.put(node.getId(), v);
					cellToNodeId.put(v, node.getId());
				}

				for (NetworkEdge edge : networkService.getEdges().values()) {
					Object from = vertices.get(edge.getFromNodeId());
					Object to = vertices.get(edge.getToNodeId());
					if (from == null || to == null) {
						continue;
					}
					graph.insertEdge(
							graphParent,
							edge.getId(),
							edgeLabel(edge),
							from,
							to,
							edgeStyle(edge)
					);
				}
			} finally {
				graph.getModel().endUpdate();
			}
			graphComponent.refresh();
		});
	}

	private String nodeLabel(NetworkNode node) {
		return switch (node.getType()) {
			case SOURCE -> node.getId();
			case CONSUMER -> node.getId();
			case JUNCTION -> node.getId();
			case INTERMEDIATE -> "";
		};
	}

	private String edgeLabel(NetworkEdge edge) {
		if (edge.getCapacity() >= 99999999.0) return "";
		double cap = edge.getCapacity();
		double flow = edge.getFlow();
		return compactNum(flow) + "/" + compactNum(cap);
	}

	private String compactNum(double v) {
		if (v >= 1000) return String.format("%dk", Math.round(v / 1000.0));
		return String.format("%.0f", v);
	}


	private String nodeStyle(NetworkNode node) {
		return switch (node.getType()) {
			case SOURCE ->
					mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_RECTANGLE + ";" +
					mxConstants.STYLE_ROUNDED + "=1;" +
					mxConstants.STYLE_FILLCOLOR + "=#EAF5FF;" +
					mxConstants.STYLE_STROKECOLOR + "=#185FA5;" +
					mxConstants.STYLE_FONTCOLOR + "=#12447C;";
			case CONSUMER ->
					mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE + ";" +
					mxConstants.STYLE_FILLCOLOR + "=#F3F3F3;" +
					mxConstants.STYLE_STROKECOLOR + "=#505050;" +
					mxConstants.STYLE_FONTCOLOR + "=#303030;";
			case JUNCTION ->
					mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE + ";" +
					mxConstants.STYLE_FILLCOLOR + "=#FDEAEA;" +
					mxConstants.STYLE_STROKECOLOR + "=#C81E1E;" +
					mxConstants.STYLE_FONTCOLOR + "=#303030;";
			case INTERMEDIATE ->
					mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_ELLIPSE + ";" +
					mxConstants.STYLE_FILLCOLOR + "=#000000;" +
					mxConstants.STYLE_STROKECOLOR + "=#000000;" +
					mxConstants.STYLE_FONTCOLOR + "=#000000;";
		};
	}

	private String edgeStyle(NetworkEdge edge) {
		return mxConstants.STYLE_STROKECOLOR + "=" + colorForLoad(edge) + ";" +
				mxConstants.STYLE_STROKEWIDTH + "=2;" +
				mxConstants.STYLE_ENDARROW + "=" + mxConstants.ARROW_CLASSIC + ";" +
				mxConstants.STYLE_FONTCOLOR + "=#222222;" +
				mxConstants.STYLE_FONTSIZE + "=8;" +
				mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD + ";" +
				mxConstants.STYLE_LABEL_BACKGROUNDCOLOR + "=#FFFFFF;" +
				mxConstants.STYLE_LABEL_BORDERCOLOR + "=" + colorForLoad(edge) + ";" +
				mxConstants.STYLE_ALIGN + "=" + mxConstants.ALIGN_CENTER + ";" +
				mxConstants.STYLE_VERTICAL_ALIGN + "=" + mxConstants.ALIGN_MIDDLE + ";" +
				mxConstants.STYLE_ROUNDED + "=1;";
	}

	private String colorForLoad(NetworkEdge edge) {
		double ratio = edge.getCapacity() > 0 ? edge.getFlow() / edge.getCapacity() : 0.0;
		if (ratio < 0.5) {
			return "#4CAF50";
		}
		if (ratio < 0.7) {
			return "#E68600";
		}
		if (ratio < 1) {
			return "#FBC02D";
		}
		if (ratio == 1) {
			return "#8a0707";
		}
		return "#D32F2F";
	}

	private int nodeWidth(NetworkNode node) {
		return switch (node.getType()) {
			case SOURCE -> 24;
			case CONSUMER -> 28;
			case JUNCTION -> 32;
			case INTERMEDIATE -> 6;
		};
	}

	private int nodeHeight(NetworkNode node) {
		return switch (node.getType()) {
			case SOURCE -> 24;
			case CONSUMER -> 28;
			case JUNCTION -> 32;
			case INTERMEDIATE -> 6;
		};
	}
}

