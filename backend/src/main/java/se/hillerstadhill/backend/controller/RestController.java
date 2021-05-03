package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import se.hillerstadhill.backend.model.database.TutorialsTablen;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {
    private CustomerRepository repository;

    public RestController(CustomerRepository repository) {
        this.repository = repository;
    }

    private String getIpAdderss(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        return ipAddress == null ? request.getRemoteAddr() : ipAddress;
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

    // GET
    @RequestMapping("/greeting")
    public String greeting(HttpSession session) {
        log.info("session: " + session.getId());
        return "Hello world";
    }

    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    @RequestMapping(path = "/postmessage", method = POST)
    public ResponseEntity<String> postMessage(
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.info("body: " + body);
        printCookies(request);
        log.info("request: " + request.getSession().getId());
        log.info("ip: " + getIpAdderss(request));
        response.addCookie(new Cookie("my", "cookie"));
        // repository.save(new TutorialsTablen());
        return new ResponseEntity<>("Get this post back", HttpStatus.OK);
    }

    @CrossOrigin(allowCredentials = "true", originPatterns = "*")
    @RequestMapping(path = "/testdb", method = POST)
    public ResponseEntity<List<TutorialsTablen>> postTestDB(
            @RequestBody String body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.info("body: " + body);
        printCookies(request);
        log.info("request: " + request.getSession().getId());

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
}
