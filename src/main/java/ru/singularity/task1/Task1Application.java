package ru.singularity.task1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.singularity.task1.service.NetworkService;
import ru.singularity.task1.ui.MainFrame;
import ru.singularity.task1.ui.panel.ControlPanel;
import ru.singularity.task1.ui.panel.NetworkPanel;
import ru.singularity.task1.ui.panel.ResultPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

@SpringBootApplication
public class Task1Application {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		ConfigurableApplicationContext ctx = SpringApplication.run(Task1Application.class, args);

		NetworkService networkService = ctx.getBean(NetworkService.class);
		networkService.createNetwork();

		if (GraphicsEnvironment.isHeadless()) {
			return;
		}

		SwingUtilities.invokeLater(() -> {
			NetworkPanel networkPanel = ctx.getBean(NetworkPanel.class);
			ControlPanel controlPanel = ctx.getBean(ControlPanel.class);
			ResultPanel resultPanel = ctx.getBean(ResultPanel.class);
			MainFrame frame = new MainFrame(networkPanel, controlPanel, resultPanel);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1920, 1080);
			frame.setVisible(true);
			networkPanel.refresh();
		});
	}

}
