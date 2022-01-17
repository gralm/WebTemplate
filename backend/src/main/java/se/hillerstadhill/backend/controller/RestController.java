package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.hillerstadhill.backend.model.User;
import se.hillerstadhill.backend.model.database.TutorialsTablen;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static se.hillerstadhill.backend.controller.UserController.COOKIE_UUID_NAME;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {
    private CustomerRepository repository;
    private UserController userController;

    public RestController(
            CustomerRepository repository,
            UserController userController
    ) {
        this.repository = repository;
        this.userController = userController;
    }

    private String getIpAdderss(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        return ipAddress == null ? request.getRemoteAddr() : ipAddress;
    }


    // GET
    @RequestMapping("/greeting")
    public String greeting(HttpSession session) {
        log.info("session: " + session.getId());
        return "Hello world";
    }

    // Override default loading index.html from static resources,
    // makes it possible to create cookies when page loads
    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    @RequestMapping(path = "/", method = GET)
    public String greeting(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = handleUser(request, response);
        log.info("User = " + user);
        try {
            return Files.readString(Paths.get(getClass().getResource("/static/index.html").toURI()));
        } catch (IOException e) {
            log.error("cant load index.html", e);
        } catch (URISyntaxException e) {
            log.error("cant load index.html", e);
        }
        return "cant load index.html";
    }


    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    @PostMapping(value = "api/{cmd}")
    public String command(
            @PathVariable(value = "cmd") String command,
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = handleUser(request, response);
        log.info("user = " + user);
        log.info("Jag kom in här, cmd = " + command);
        log.info("Jag kom in här, cmd2 = " + body);
        return command;
    }


    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    @RequestMapping(path = "/postmessage", method = POST)
    public ResponseEntity<String> postMessage(
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = handleUser(request, response);
        log.info("User: " + user);
        log.info("body: " + body);
        String responseMessage = "skicka tillbaka det här";


        // hander.fixReply()
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    @RequestMapping(path = "/testdb", method = POST)
    public ResponseEntity<List<TutorialsTablen>> postTestDB(
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = handleUser(request, response);

        log.info("User: " + user);

        /*
        TODO: Warning message in browser:
        Cookie “my” will be soon rejected because it has the “SameSite” attribute set to “None” or an invalid value,
        without the “secure” attribute. To know more about the “SameSite“ attribute, read
        https://developer.mozilla.org/docs/Web/HTTP/Headers/Set-Cookie/SameSite
         */

        response.addCookie(new Cookie("my", "cookie"));
        TutorialsTablen row = new TutorialsTablen(
                body,
                getIpAdderss(request),
                request.getSession().getId()
        );
        log.info("Saving: " + row);
        repository.save(row);
        Iterable<TutorialsTablen> asdf = repository.findAll();
        List<TutorialsTablen> tutorialsTablens = new ArrayList<>();
        asdf.forEach(tutorialsTablen -> tutorialsTablens.add(tutorialsTablen));
        return new ResponseEntity<>(tutorialsTablens, HttpStatus.OK);
    }


    private void createCookie(HttpServletResponse response, String name, String value) {
        response.addCookie(new Cookie(name, value));
    }
    private void printCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("cookies = null");
            return;
        }
        log.info("num of cookies: " + cookies.length);
        for (Cookie cookie : cookies) {
            log.info("cookie: " + cookie.getName() + "; " + cookie.getValue());
        }
    }

    private UUID getUuidCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals(COOKIE_UUID_NAME)) {
                try {
                    return UUID.fromString(cookie.getValue());
                } catch (IllegalArgumentException e) {
                    log.error("getUuidCookie failed: in cookie: " + cookie.getValue());
                }
            }
        }
        return null;
    }

    private User handleUser(HttpServletRequest request, HttpServletResponse response) {
        User user = this.userController.getUserByRequestCookie(request);
        String ip = getIpAdderss(request);
        UUID frontendUuid = getUuidCookie(request);
        if (user != null) {
            user.update();
        } else if (frontendUuid == null) {
            user = new User(null, null, ip);
            log.info("new user created with uuid");
            userController.createUser(user);
            response.addCookie(new Cookie(COOKIE_UUID_NAME, user.getUuid().toString()));
        } else {
            user = new User(frontendUuid, null, ip);
            userController.createUser(user);
        }
        return user;
    }
}
