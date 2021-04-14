package se.hillerstadhill.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.hillerstadhill.backend.model.database.TutorialsTablen;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repo;

    @Test
    public void testH2DatabaseWorksInTest() {
        String tutAuthor = "tutAuthor";
        String tutTitle = "tutTitle";
        repo.save(new TutorialsTablen(tutTitle, tutAuthor));
        assertThat(repo.count(), is(2L));
        assertThat(repo.findByTutorialAuthor(tutAuthor).get(0).getTutorialTitle(), is(tutTitle));
    }
}