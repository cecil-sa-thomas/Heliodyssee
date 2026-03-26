package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AccessController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String adminBackoffice() {
        return "admin/dashboard"; // Chemin simplifié
    }
}
