package ru.singularity.task1.ui.panel;

import org.springframework.stereotype.Component;
import ru.singularity.task1.model.NetworkEdge;
import ru.singularity.task1.model.NetworkNode;
import ru.singularity.task1.service.NetworkService;

import javax.swing.*;
import java.awt.*;

// ui/panel/NetworkPanel.java
@Component
public class NetworkPanel extends JPanel {

	// Никакого HTTP — прямой вызов Spring-сервиса
	private final NetworkService networkService;

	public NetworkPanel(NetworkService networkService) {
		this.networkService = networkService;
		setupMouseListeners();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Читаем прямо из сервиса — данные уже в памяти
		networkService.getEdges().values().forEach(e -> drawEdge(g2, e));
		networkService.getNodes().values().forEach(n -> drawNode(g2, n));
	}

	private void drawEdge(Graphics2D g2, NetworkEdge edge) {
		NetworkNode from = networkService.getNode(edge.getFromNodeId());
		NetworkNode to   = networkService.getNode(edge.getToNodeId());
		if (from == null || to == null) return;

		float ratio = edge.getCapacity() > 0
				? (float)(edge.getFlow() / edge.getCapacity()) : 0f;

		// Зелёный -> жёлтый -> красный по загрузке линии
		g2.setColor(loadColor(ratio));
		g2.setStroke(new BasicStroke(2.0f));
		g2.drawLine((int)from.getX(), (int)from.getY(),
				(int)to.getX(),   (int)to.getY());

		// Подпись потока
		g2.setFont(new Font("Arial", Font.PLAIN, 10));
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(
				String.format("%.1f / %.1f", edge.getFlow(), edge.getCapacity()),
				(int)((from.getX() + to.getX()) / 2),
				(int)((from.getY() + to.getY()) / 2) - 5
		);
	}

	private void drawNode(Graphics2D g2, NetworkNode node) {
		switch (node.getType()) {
			case SOURCE   -> drawSource(g2, node);
			case CONSUMER -> drawConsumer(g2, node);
			case JUNCTION -> drawJunction(g2, node);
		}
	}

	// Источник питания — прямоугольник с символом ||
	private void drawSource(Graphics2D g2, NetworkNode node) {
		int x = (int) node.getX();
		int y = (int) node.getY();

		// Прямоугольник с подписью
		int w = 50, h = 24;
		g2.setColor(new Color(230, 245, 255));
		g2.fillRoundRect(x - w/2, y - h/2, w, h, 6, 6);
		g2.setColor(new Color(24, 95, 165));
		g2.setStroke(new BasicStroke(1.2f));
		g2.drawRoundRect(x - w/2, y - h/2, w, h, 6, 6);

		// ID узла внутри прямоугольника
		g2.setFont(new Font("Arial", Font.BOLD, 10));
		g2.setColor(new Color(12, 68, 124));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(node.getId(),
				x - fm.stringWidth(node.getId()) / 2,
				y + fm.getAscent() / 2 - 1
		);

		// Символ источника || над прямоугольником
		g2.setColor(new Color(24, 95, 165));
		g2.setStroke(new BasicStroke(2.0f));
		g2.drawLine(x - 5, y - h/2 - 10, x - 5, y - h/2 - 2);  // левая черта
		g2.drawLine(x + 5, y - h/2 - 10, x + 5, y - h/2 - 2);  // правая черта

		// Мощность источника под прямоугольником
		if (node.getCapacity() > 0) {
			g2.setFont(new Font("Arial", Font.PLAIN, 9));
			g2.setColor(Color.DARK_GRAY);
			String cap = String.format("%.0f МВт", node.getCapacity());
			g2.drawString(cap,
					x - fm.stringWidth(cap) / 2,
					y + h/2 + 12
			);
		}
	}

	// Потребитель — перечёркнутый круг (⊗) с синей точкой
	private void drawConsumer(Graphics2D g2, NetworkNode node) {
		int x = (int) node.getX();
		int y = (int) node.getY();
		int r = 10;

		// Заливка круга
		g2.setColor(new Color(240, 240, 240));
		g2.fillOval(x - r, y - r, r * 2, r * 2);

		// Обводка
		g2.setColor(new Color(80, 80, 80));
		g2.setStroke(new BasicStroke(1.2f));
		g2.drawOval(x - r, y - r, r * 2, r * 2);

		// Крест внутри круга (символ ⊗)
		g2.drawLine(x - r + 3, y - r + 3, x + r - 3, y + r - 3);
		g2.drawLine(x + r - 3, y - r + 3, x - r + 3, y + r - 3);

		// Синяя точка — признак потребителя (как на схеме из PDF)
		g2.setColor(new Color(55, 138, 221));
		g2.fillOval(x - 4, y - 4, 8, 8);

		// ID узла рядом
		g2.setFont(new Font("Arial", Font.PLAIN, 10));
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(node.getId(), x + r + 4, y + 4);

		// Потребление под узлом
		if (node.getDemand() > 0) {
			g2.setFont(new Font("Arial", Font.PLAIN, 9));
			g2.setColor(Color.GRAY);
			g2.drawString(
					String.format("%.0f МВт", node.getDemand()),
					x - r, y + r + 12
			);
		}
	}

	private void drawJunction(Graphics2D g2, NetworkNode node) {
		int r = 18;
		g2.setColor(new Color(220, 50, 50, 50));
		g2.fillOval((int)node.getX()-r, (int)node.getY()-r, r*2, r*2);
		g2.setColor(new Color(200, 30, 30));
		g2.setStroke(new BasicStroke(1.5f));
		g2.drawOval((int)node.getX()-r, (int)node.getY()-r, r*2, r*2);
		g2.setFont(new Font("Arial", Font.BOLD, 10));
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(node.getId(), (int)node.getX()-6, (int)node.getY()+4);
	}

	// Перерисовка при изменении данных
	public void refresh() {
		SwingUtilities.invokeLater(this::repaint);
	}

	private Color loadColor(float r) {
		r = Math.max(0f, Math.min(1f, r));
		return r < 0.5f
				? new Color((int)(r * 2 * 255), 200, 0)
				: new Color(255, (int)((1 - (r-0.5f)*2) * 200), 0);
	}

	private void setupMouseListeners() {
		addMouseWheelListener(e -> {
			// zoom — реализуется через AffineTransform
		});
	}
}

