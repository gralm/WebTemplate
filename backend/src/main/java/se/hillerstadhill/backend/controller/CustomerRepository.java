package se.hillerstadhill.backend.controller;

import org.springframework.data.repository.CrudRepository;
import se.hillerstadhill.backend.model.database.TutorialsTablen;

import java.util.List;

public interface CustomerRepository extends CrudRepository<TutorialsTablen, Long> {
    TutorialsTablen findById(long id);
    List<TutorialsTablen> findBySessionId(String sessionId);
    // List<TutorialsTablen> findByTutorialAuthor(String tutorialAuthor);
}
