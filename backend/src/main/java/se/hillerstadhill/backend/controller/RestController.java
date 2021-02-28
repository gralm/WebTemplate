package se.hillerstadhill.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class RestController {

    public RestController() {
        System.out.println("konstruktor");
    }

    // GET

    @RequestMapping("/greeting")
    public String greeting() {
        return "Hello world";
    }


    @RequestMapping(path = "/asdf", method = POST)
    public String asdf() {
        return "Skicka tillbaka detta";
    }
}
