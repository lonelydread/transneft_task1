package ru.singularity.task1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.singularity.task1.ui.panel.NetworkPanel;

@SpringBootTest
class Task1ApplicationTests {

	@MockitoBean
	private NetworkPanel networkPanel;

	@Test
	void contextLoads() {
	}

}
