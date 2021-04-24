package se.hillerstadhill.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

    public BasicApplication(ConfigurableEnvironment env) {
        System.out.println("profiles: [" +
                Arrays.asList(env.getActiveProfiles()).stream().collect(Collectors.joining(", ")) +
                "]");
    }
}
