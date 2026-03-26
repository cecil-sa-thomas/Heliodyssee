package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegalController {

    @GetMapping("/legal/privacy-policy")
    public String cookiesPolicy() {
        return "legal/privacy-policy";
    }
}