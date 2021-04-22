package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/*
@CrossOrigin must have these parameters to get successful response with cookies
 */
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {

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

    @RequestMapping(path = "/postmessage", method = POST)
    public ResponseEntity<String> postMessage(HttpServletRequest request, HttpServletResponse response) {
        printCookies(request);
        log.info("request: " + request.getSession().getId());
        log.info("ip: " + getIpAdderss(request));

        return new ResponseEntity<>("Get this post back", HttpStatus.OK);
    }
}
