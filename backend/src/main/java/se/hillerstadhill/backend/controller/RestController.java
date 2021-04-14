package se.hillerstadhill.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {

    public RestController() {
        log.info("RestController constructor");
    }

    // GET
    @RequestMapping("/greeting")
    public String greeting() {
        return "Hello world";
    }


    @RequestMapping(path = "/postmessage", method = POST)
    public String postMessage() {
        return "Get this post back";
    }
}
