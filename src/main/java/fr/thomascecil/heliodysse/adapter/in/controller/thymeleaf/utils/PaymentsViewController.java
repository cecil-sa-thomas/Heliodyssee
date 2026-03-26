package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf.utils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentsViewController {

    @GetMapping("/confirmation")
    public String showConfirmationPage() {
        return "confirmation"; // retournera templates/confirmation.html
    }
}
