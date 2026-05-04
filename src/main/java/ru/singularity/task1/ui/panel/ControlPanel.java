package ru.singularity.task1.ui.panel;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import ru.singularity.task1.model.OptimizationResult;
import ru.singularity.task1.service.NetworkService;
import ru.singularity.task1.service.OptimizationService;
import ru.singularity.task1.service.ShapService;
import ru.singularity.task1.ui.InterpretationDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Component
@ConditionalOnExpression("!T(java.awt.GraphicsEnvironment).isHeadless()")
public class ControlPanel extends JPanel {

	private final NetworkService networkService;
	private final OptimizationService optimizationService;
	private final ShapService shapService;
	private final NetworkPanel networkPanel;
	private final ResultPanel resultPanel;

	private OptimizationResult lastMlResult;
	private OptimizationResult lastLpResult;

	private JButton btnSaveMl;
	private JButton btnSaveLp;

	public ControlPanel(NetworkService networkService,
						OptimizationService optimizationService,
						ShapService shapService,
						NetworkPanel networkPanel,
						ResultPanel resultPanel) {
		this.networkService      = networkService;
		this.optimizationService = optimizationService;
		this.shapService         = shapService;
		this.networkPanel        = networkPanel;
		this.resultPanel         = resultPanel;
		buildUI();
	}

	private void buildUI() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(145, 0));

		JButton btnMl        = new JButton("<html><center>Решить с помощью ИИ</center></html>");
		JButton btnLp        = new JButton("<html><center>Решить с помощью алгоритма</center></html>");
		JButton btnInterpret = new JButton("<html><center>Интерпретировать результаты ИИ</center></html>");
		btnSaveMl            = new JButton("<html><center>Сохранить результат работы ИИ</center></html>");
		btnSaveLp            = new JButton("<html><center>Сохранить результат работы алгоритма</center></html>");

		btnSaveMl.setEnabled(false);
		btnSaveLp.setEnabled(false);

		for (JButton btn : new JButton[]{btnMl, btnLp, btnInterpret, btnSaveMl, btnSaveLp}) {
			btn.setPreferredSize(new Dimension(135, 48));
			btn.setMinimumSize(new Dimension(80, 48));
			btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
			btn.setAlignmentX(0.5f);
		}

		btnMl.addActionListener(e -> runOptimization(btnMl, optimizationService::optimize, r -> {
			lastMlResult = r;
			btnSaveMl.setEnabled(true);
		}));
		btnLp.addActionListener(e -> runOptimization(btnLp, optimizationService::optimizeLp, r -> {
			lastLpResult = r;
			btnSaveLp.setEnabled(true);
		}));
		btnInterpret.addActionListener(e -> {
			Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
			new InterpretationDialog(parent, shapService).setVisible(true);
		});
		btnSaveMl.addActionListener(e -> saveResult(lastMlResult, "flows_ml.csv"));
		btnSaveLp.addActionListener(e -> saveResult(lastLpResult, "flows_lp.csv"));

		add(btnMl);
		add(Box.createRigidArea(new Dimension(0, 6)));
		add(btnLp);
		add(Box.createRigidArea(new Dimension(0, 6)));
		add(btnInterpret);
		add(Box.createRigidArea(new Dimension(0, 6)));
		add(btnSaveMl);
		add(Box.createRigidArea(new Dimension(0, 6)));
		add(btnSaveLp);
	}

	private void runOptimization(JButton source, Supplier<OptimizationResult> task,
								 Consumer<OptimizationResult> onSuccess) {
		source.setEnabled(false);
		new SwingWorker<OptimizationResult, Void>() {
			@Override
			protected OptimizationResult doInBackground() {
				return task.get();
			}
			@Override
			protected void done() {
				try {
					OptimizationResult result = get();
					networkService.applyFlows(result.getFlows());
					resultPanel.showResult(result);
					networkPanel.refresh();
					onSuccess.accept(result);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(ControlPanel.this,
							"Ошибка: " + ex.getCause().getMessage(),
							"Ошибка оптимизации", JOptionPane.ERROR_MESSAGE);
				} finally {
					source.setEnabled(true);
				}
			}
		}.execute();
	}


	private void saveResult(OptimizationResult result, String defaultName) {
		if (result == null) {
			JOptionPane.showMessageDialog(this, "Сначала выполните расчёт.",
					"Нет данных", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Сохранить результат");
		chooser.setSelectedFile(new File(defaultName));
		chooser.setFileFilter(new FileNameExtensionFilter("CSV файлы (*.csv)", "csv"));

		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

		File file = chooser.getSelectedFile();
		if (!file.getName().toLowerCase().endsWith(".csv"))
			file = new File(file.getPath() + ".csv");

		Map<String, Double> flows = result.getFlows();

		TreeSet<String> nodeSet = new TreeSet<>();
		for (String key : flows.keySet()) {
			String[] p = key.split("->");
			if (p.length == 2) { nodeSet.add(p[0]); nodeSet.add(p[1]); }
		}
		List<String> nodes = new ArrayList<>(nodeSet);

		Map<String, Map<String, Double>> matrix = new LinkedHashMap<>();
		for (String n : nodes) matrix.put(n, new LinkedHashMap<>());
		for (Map.Entry<String, Double> e : flows.entrySet()) {
			String[] p = e.getKey().split("->");
			if (p.length == 2 && e.getValue() > 0)
				matrix.get(p[0]).put(p[1], e.getValue());
		}

		try (PrintWriter pw = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(file), Charset.forName("cp1251")))) {

			pw.println(";" + String.join(";", nodes));

			for (String row : nodes) {
				StringBuilder sb = new StringBuilder(row);
				for (String col : nodes) {
					sb.append(";");
					Double val = matrix.get(row).get(col);
					if (val != null)
						sb.append(String.format("%.3f", val).replace(".", ","));
				}
				pw.println(sb);
			}

			JOptionPane.showMessageDialog(this,
					"Файл сохранён:\n" + file.getAbsolutePath(),
					"Готово", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this,
					"Ошибка сохранения: " + ex.getMessage(),
					"Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}
}
