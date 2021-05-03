package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

// Inspired from https://stackoverflow.com/questions/41757162/connect-to-web-socket-server-at-runtime-from-back-end-java-class
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class WebsocketControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SocketTextHandler socketTextHandler;

    @BeforeEach
    public void setup() {
        log.info("Start up webenvironment on port " + port);
    }

    @Test
    public void testSocketTextHandlerIdentifiesSocketConnection() throws Exception {
        String uri = "ws://localhost:" + port + "/user";
        assertThat(socketTextHandler.sessions.size(), is(equalTo(0)));

        final CountDownLatch latch = new CountDownLatch(1);
        EchoHandler handler = new EchoHandler(latch);
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketSession session = client.doHandshake(handler, uri).get();
        session.sendMessage(new TextMessage("Hello World"));
        latch.await(5000, TimeUnit.SECONDS);
        assertThat(socketTextHandler.sessions.size(), is(equalTo(1)));

        session.close();
        Thread.sleep(300L);
        assertThat(socketTextHandler.sessions.size(), is(equalTo(0)));
    }

    private class EchoHandler extends TextWebSocketHandler {

        private final CountDownLatch latch;

        public EchoHandler(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) {
            System.out.println("------- received client message ------");
            System.out.println(message.getPayload());
            System.out.println("--------- end client message ---------");
            latch.countDown();
        }
    }
}