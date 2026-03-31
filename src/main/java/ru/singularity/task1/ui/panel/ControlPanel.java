package ru.singularity.task1.ui.panel;

import org.springframework.stereotype.Component;
import ru.singularity.task1.model.OptimizationResult;
import ru.singularity.task1.service.NetworkService;
import ru.singularity.task1.service.OptimizationService;

import javax.swing.*;
import java.awt.*;

// ui/panel/ControlPanel.java
@Component
public class ControlPanel extends JPanel {

	private final NetworkService networkService;
	private final OptimizationService optimizationService;
	private final NetworkPanel        networkPanel;   // для refresh()

	public ControlPanel(NetworkService networkService,
						OptimizationService optimizationService,
						NetworkPanel networkPanel) {
		this.networkService      = networkService;
		this.optimizationService = optimizationService;
		this.networkPanel        = networkPanel;
		buildUI();
	}

	private void buildUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(220, 0));

		JButton btnMaxFlow  = new JButton("Max Flow");
		JButton btnMinCost  = new JButton("Min Cost Flow");
		JButton btnForecast = new JButton("ML Прогноз");
		JButton btnClear    = new JButton("Очистить сеть");

		btnMaxFlow.addActionListener(e -> runOptimization("max_flow"));
		btnMinCost.addActionListener(e -> runOptimization("min_cost"));
		btnForecast.addActionListener(e -> runOptimization("ml_forecast"));
		btnClear.addActionListener(e -> {
			networkService.clearNetwork();
			networkPanel.refresh();
		});

		add(btnMaxFlow);
		add(btnMinCost);
		add(btnForecast);
		add(Box.createVerticalStrut(20));
		add(btnClear);
	}

	private void runOptimization(String algorithm) {
		// SwingWorker — оптимизация в фоне, UI не замерзает
		new SwingWorker<OptimizationResult, Void>() {
			@Override
			protected OptimizationResult doInBackground() {
				// Прямой вызов сервиса (сервис сам вызывает Python)
				return optimizationService.optimize(algorithm);
			}
			@Override
			protected void done() {
				networkPanel.refresh(); // обновляем визуализацию
			}
		}.execute();
	}
}

