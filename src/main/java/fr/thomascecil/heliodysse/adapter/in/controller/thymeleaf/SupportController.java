package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SupportController {

    @GetMapping("/support")
    public String showSupportForm() {
        return "fragments/session/user-menu/support/support-form";
    }

    @PostMapping("/support")
    public String handleSupportForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String message
    ) {
        // Ici, tu pourrais logguer ou simuler un envoi d'email
        System.out.println("Assistance reçue de : " + name + " (" + email + ")");
        System.out.println("Message : " + message);
        return "fragments/session/user-menu/support/support-confirmation";
    }
}
