package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf;

import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String displayIndex(Model model,
                               @AuthenticationPrincipal UserDetailsImpl user,
                               @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("isAuthenticated", user != null);
        model.addAttribute("user", user);

        if (error != null) {
            model.addAttribute("loginError", true);
        }

        return "index";
    }

    @GetMapping("/")
    public String accueilPublic() {
        return "redirect:/index";
    }
}
