package se.hillerstadhill.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import se.hillerstadhill.backend.controller.CustomerRepository;
import se.hillerstadhill.backend.model.database.TutorialsTablen;

@SpringBootApplication
@Slf4j
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            int rand = (int) (Math.random() * 10000);
            System.out.println("Bauer" + rand);
            repository.save(new TutorialsTablen("Jack", "Bauer" + rand));
            for (TutorialsTablen tutorialstbl : repository.findAll()) {
                log.info(tutorialstbl.toString());
            }
            log.info("id: " + repository.findByTutorialAuthor("Bauer3148"));
        };
    }
}
