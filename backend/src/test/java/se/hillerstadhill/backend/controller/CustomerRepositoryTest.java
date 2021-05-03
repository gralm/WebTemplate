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
        repo.save(new TutorialsTablen("Hello1", "Ip1", "session1"));
        repo.save(new TutorialsTablen("Hello2", "Ip2", "session2"));
        repo.save(new TutorialsTablen("Hello3", "Ip1", "session1"));

        assertThat(repo.count(), is(3L));
        assertThat(repo.findBySessionId("session2").get(0).getIp(), is("Ip2"));
        assertThat(repo.findBySessionId("session1").size(), is(2));
    }
}