package ru.singularity.task1.ui.panel;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.singularity.task1.model.NetworkEdge;
import ru.singularity.task1.model.NetworkNode;
import ru.singularity.task1.service.NetworkService;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.util.List;

@Component
@ConditionalOnExpression("!T(java.awt.GraphicsEnvironment).isHeadless()")
public class NetworkPanel extends JPanel {

	private static final int   DOT_COUNT        = 3;
	private static final int   DOT_SIZE         = 8;
	private static final int   TIMER_INTERVAL   = 40;

	private final NetworkService networkService;
	private final mxGraph graph;
	private final Object graphParent;
	private final mxGraphComponent graphComponent;
	private final Map<Object, String> cellToNodeId = new HashMap<>();

	// animation state
	private final Set<Object>     markerCells  = new HashSet<>();
	private final List<FlowMarker> flowMarkers = new ArrayList<>();
	private Timer animationTimer;

	public NetworkPanel(NetworkService networkService) {
		this.networkService = networkService;
		this.graph = new mxGraph() {
			@Override public boolean isCellEditable(Object cell)   { return false; }
			@Override public boolean isCellSelectable(Object cell) { return !markerCells.contains(cell); }
			@Override public boolean isCellMovable(Object cell)    { return !markerCells.contains(cell); }
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
		graphComponent.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				fitGraph();
			}
		});
		refresh();
	}

	public void refresh() {
		SwingUtilities.invokeLater(() -> {
			stopAnimation();

			graph.getModel().beginUpdate();
			try {
				graph.removeCells(graph.getChildVertices(graphParent));
				graph.removeCells(graph.getChildEdges(graphParent));

				cellToNodeId.clear();
				Map<String, Object> vertices = new HashMap<>();
				for (NetworkNode node : networkService.getNodes().values()) {
					Object v = graph.insertVertex(
							graphParent, node.getId(), nodeLabel(node),
							node.getX(), node.getY(),
							nodeWidth(node), nodeHeight(node),
							nodeStyle(node));
					vertices.put(node.getId(), v);
					cellToNodeId.put(v, node.getId());
				}

				for (NetworkEdge edge : networkService.getEdges().values()) {
					Object from = vertices.get(edge.getFromNodeId());
					Object to   = vertices.get(edge.getToNodeId());
					if (from == null || to == null) continue;
					graph.insertEdge(graphParent, edge.getId(), edgeLabel(edge),
							from, to, edgeStyle(edge));
				}
			} finally {
				graph.getModel().endUpdate();
			}

			startAnimation();
			graphComponent.refresh();
			SwingUtilities.invokeLater(this::fitGraph);
		});
	}

	private void fitGraph() {
		mxRectangle raw = graph.getGraphBounds();
		if (raw == null || raw.getWidth() == 0 || raw.getHeight() == 0) return;
		int cw = graphComponent.getWidth();
		int ch = graphComponent.getHeight();
		if (cw <= 0 || ch <= 0) return;

		double s0  = graph.getView().getScale();
		double tx0 = graph.getView().getTranslate().getX();
		double ty0 = graph.getView().getTranslate().getY();

		double minModelX = raw.getX()      / s0 - tx0;
		double minModelY = raw.getY()      / s0 - ty0;
		double modelW    = raw.getWidth()  / s0;
		double modelH    = raw.getHeight() / s0;

		int pad = 20;
		double newScale = Math.min(
			(cw - 2.0 * pad) / modelW,
			(ch - 2.0 * pad) / modelH
		);

		graph.getView().scaleAndTranslate(
			newScale,
			pad / newScale - minModelX,
			pad / newScale - minModelY
		);
	}

	// Анимация

	private void stopAnimation() {
		if (animationTimer != null) {
			animationTimer.stop();
			animationTimer = null;
		}
		markerCells.clear();
		flowMarkers.clear();
	}

	private void startAnimation() {
		graph.getModel().beginUpdate();
		try {
			for (NetworkEdge edge : networkService.getEdges().values()) {
				if (edge.getFlow() <= 0) continue;

				NetworkNode fn = networkService.getNode(edge.getFromNodeId());
				NetworkNode tn = networkService.getNode(edge.getToNodeId());
				if (fn == null || tn == null) continue;

				double fx = fn.getX() + nodeWidth(fn)  / 2.0;
				double fy = fn.getY() + nodeHeight(fn) / 2.0;
				double tx = tn.getX() + nodeWidth(tn)  / 2.0;
				double ty = tn.getY() + nodeHeight(tn) / 2.0;

				double ratio = edge.getCapacity() > 0 ? edge.getFlow() / edge.getCapacity() : 0.5;
				double speed = 0.016 * Math.max(0.4, Math.min(2.0, ratio + 0.4));
				String style = dotStyle(colorForLoad(edge));

				for (int i = 0; i < DOT_COUNT; i++) {
					double p  = (double) i / DOT_COUNT;
					double mx = fx + (tx - fx) * p - DOT_SIZE / 2.0;
					double my = fy + (ty - fy) * p - DOT_SIZE / 2.0;
					Object cell = graph.insertVertex(graphParent, null, "",
							mx, my, DOT_SIZE, DOT_SIZE, style);
					markerCells.add(cell);
					flowMarkers.add(new FlowMarker(cell, fx, fy, tx, ty, p, speed));
				}
			}
		} finally {
			graph.getModel().endUpdate();
		}

		if (!markerCells.isEmpty()) {
			graph.orderCells(true, markerCells.toArray());
		}

		animationTimer = new Timer(TIMER_INTERVAL, e -> tickAnimation());
		animationTimer.start();
	}

	private void tickAnimation() {
		graph.getModel().beginUpdate();
		try {
			for (FlowMarker m : flowMarkers) {
				m.progress += m.speed;
				if (m.progress >= 1.0) m.progress -= 1.0;

				double nx = m.fromX + (m.toX - m.fromX) * m.progress - DOT_SIZE / 2.0;
				double ny = m.fromY + (m.toY - m.fromY) * m.progress - DOT_SIZE / 2.0;
				mxGeometry geo = (mxGeometry) graph.getModel().getGeometry(m.cell).clone();
				geo.setX(nx);
				geo.setY(ny);
				graph.getModel().setGeometry(m.cell, geo);
			}
		} finally {
			graph.getModel().endUpdate();
		}
	}

	private String dotStyle(String color) {
		return mxConstants.STYLE_SHAPE      + "=" + mxConstants.SHAPE_ELLIPSE + ";" +
			   mxConstants.STYLE_FILLCOLOR  + "=" + color + ";" +
			   mxConstants.STYLE_STROKECOLOR + "=none;" +
			   mxConstants.STYLE_NOLABEL    + "=1;" +
			   "opacity=75;";
	}

	private static class FlowMarker {
		final Object cell;
		final double fromX, fromY, toX, toY;
		double progress;
		final double speed;

		FlowMarker(Object cell,
				   double fromX, double fromY,
				   double toX,   double toY,
				   double progress, double speed) {
			this.cell = cell;
			this.fromX = fromX; this.fromY = fromY;
			this.toX   = toX;   this.toY   = toY;
			this.progress = progress;
			this.speed    = speed;
		}
	}

	// Стили

	private String nodeLabel(NetworkNode node) {
		return switch (node.getType()) {
			case SOURCE, CONSUMER, JUNCTION -> displayId(node.getId());
			case INTERMEDIATE -> "";
		};
	}

	private static String displayId(String id) {
		return "I.1".equals(id) ? "I" : id;
	}

	private String edgeLabel(NetworkEdge edge) {
		if (edge.getCapacity() >= 99999999.0) return "";
		return compactNum(edge.getFlow()) + "/" + compactNum(edge.getCapacity());
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
				mxConstants.STYLE_STROKEWIDTH + "=3;" +
				mxConstants.STYLE_ENDARROW + "=" + mxConstants.ARROW_CLASSIC + ";" +
				mxConstants.STYLE_FONTCOLOR + "=#222222;" +
				mxConstants.STYLE_FONTSIZE + "=10;" +
				mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD + ";" +
				mxConstants.STYLE_LABEL_BACKGROUNDCOLOR + "=#FFFFFF;" +
				mxConstants.STYLE_LABEL_BORDERCOLOR + "=" + colorForLoad(edge) + ";" +
				mxConstants.STYLE_ALIGN + "=" + mxConstants.ALIGN_CENTER + ";" +
				mxConstants.STYLE_VERTICAL_ALIGN + "=" + mxConstants.ALIGN_MIDDLE + ";" +
				mxConstants.STYLE_ROUNDED + "=1;";
	}

	private String colorForLoad(NetworkEdge edge) {
		NetworkNode from = networkService.getNode(edge.getFromNodeId());
		if (from != null && from.getType() == NetworkNode.NodeType.SOURCE) return "#185FA5";
		double ratio = edge.getCapacity() > 0 ? edge.getFlow() / edge.getCapacity() : 0.0;
		if (ratio < 0.3) return "#43A047";
		if (ratio < 0.5) return "#FDD835";
		if (ratio < 0.7) return "#FB8C00";
		if (ratio < 0.9) return "#E53935";
		return "#B71C1C";
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
