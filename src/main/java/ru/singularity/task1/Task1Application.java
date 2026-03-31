package ru.singularity.task1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.singularity.task1.ui.MainFrame;

import javax.swing.*;

@SpringBootApplication
public class Task1Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =
				SpringApplication.run(Task1Application.class, args);

		SwingUtilities.invokeLater(() -> {
			MainFrame frame = ctx.getBean(MainFrame.class);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1400, 900);
			frame.setVisible(true);
		});
	}

}
