package ru.singularity.task1.ui.panel;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.singularity.task1.model.OptimizationResult;
import ru.singularity.task1.service.NetworkService;
import ru.singularity.task1.service.OptimizationService;


import javax.swing.*;
import java.awt.*;

// ui/panel/ControlPanel.java
@Component
@ConditionalOnExpression("!T(java.awt.GraphicsEnvironment).isHeadless()")
public class ControlPanel extends JPanel {

	private final NetworkService networkService;
	private final OptimizationService optimizationService;
	private final NetworkPanel networkPanel;
	private final ResultPanel resultPanel;

	public ControlPanel(NetworkService networkService,
						OptimizationService optimizationService,
						NetworkPanel networkPanel,
						ResultPanel resultPanel) {
		this.networkService      = networkService;
		this.optimizationService = optimizationService;
		this.networkPanel        = networkPanel;
		this.resultPanel         = resultPanel;
		buildUI();
	}

	private void buildUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(160, 0));

		JButton btnOptimize   = new JButton("Отобразить заявки");

		btnOptimize.addActionListener(e -> {
			btnOptimize.setEnabled(false);
			new SwingWorker<OptimizationResult, Void>() {
				@Override
				protected OptimizationResult doInBackground() {
					return optimizationService.optimize();
				}
				@Override
				protected void done() {
					try {
						OptimizationResult result = get();
						networkService.applyFlows(result.getFlows());
						resultPanel.showResult(result);
						networkPanel.refresh();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(ControlPanel.this,
								"Ошибка: " + ex.getCause().getMessage(),
								"Ошибка оптимизации", JOptionPane.ERROR_MESSAGE);
					} finally {
						btnOptimize.setEnabled(true);
					}
				}
			}.execute();
		});

		add(btnOptimize);
	}

//	private void runOptimization(String algorithm) {
//		// SwingWorker — оптимизация в фоне, UI не замерзает
//		new SwingWorker<OptimizationResult, Void>() {
//			@Override
//			protected OptimizationResult doInBackground() {
//				// Прямой вызов сервиса (сервис сам вызывает Python)
//				return optimizationService.optimize(algorithm);
//			}
//			@Override
//			protected void done() {
//				networkPanel.refresh(); // обновляем визуализацию
//			}
//		}.execute();
//	}
}

