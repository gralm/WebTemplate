package se.hillerstadhill.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BasicApplicationTests {

	@Value("${example.firstProperty}")
	private String firstProperty;

	@Test
	void contextLoads() {
		Assertions.assertEquals("Hello2", firstProperty);
	}

}
