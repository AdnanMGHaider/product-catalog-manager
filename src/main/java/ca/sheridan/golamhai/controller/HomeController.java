package ca.sheridan.golamhai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Public landing page.
     * Maps "/" to landing.html.
     */
    @GetMapping("/")
    public String landingPage() {
        return "landing";
    }
}
