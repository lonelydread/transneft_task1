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

		JButton btnClear    = new JButton("Очистить сеть");


		btnClear.addActionListener(e -> {
			networkService.clearNetwork();
			networkPanel.refresh();
		});

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

