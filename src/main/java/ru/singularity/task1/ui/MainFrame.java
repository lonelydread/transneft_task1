package ru.singularity.task1.ui;

import org.springframework.stereotype.Component;
import ru.singularity.task1.ui.panel.ControlPanel;
import ru.singularity.task1.ui.panel.NetworkPanel;
import ru.singularity.task1.ui.panel.ResultPanel;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;

// ui/MainFrame.java
@Component                          // ← Spring управляет жизненным циклом
public class MainFrame extends JFrame {

	private final NetworkPanel networkPanel;
	private final ControlPanel controlPanel;
	private final ResultPanel  resultPanel;

	// Spring сам инжектирует все панели через конструктор
	public MainFrame(NetworkPanel networkPanel,
					 ControlPanel controlPanel,
					 ResultPanel resultPanel) {
		super("Energy Flow Optimizer");
		this.networkPanel  = networkPanel;
		this.controlPanel  = controlPanel;
		this.resultPanel   = resultPanel;
		initLayout();
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		add(networkPanel,  BorderLayout.CENTER);
		add(controlPanel,  BorderLayout.WEST);
		add(resultPanel,   BorderLayout.EAST);
	}
}

