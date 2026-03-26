package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf.admin;

import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.port.in.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UsersController {

    @Autowired
    private UserUseCase userUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String showUserList(Model model) {
        List<User> users = userUseCase.getAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String showUserDetail(@PathVariable Long id, @RequestParam(value = "edit", required = false) Boolean edit, Model model) {
        User user = userUseCase.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        model.addAttribute("user", user);
        model.addAttribute("editMode", Boolean.TRUE.equals(edit));
        return "admin/user-details";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        try {
            user.setIdUser(id); // pour être sûr que l'ID reste correct
            userUseCase.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "Utilisateur mis à jour !");
            return "redirect:/admin/users/" + id;
        } catch (IllegalArgumentException e) {
            // En cas d'erreur de validation, on reste sur la page d'édition
            model.addAttribute("user", user);
            model.addAttribute("editMode", true);
            model.addAttribute("error", e.getMessage());
            return "admin/user-details";
        } catch (Exception e) {
            // Autres erreurs
            model.addAttribute("user", user);
            model.addAttribute("editMode", true);
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "admin/user-details";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userUseCase.deleteUserById(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé avec succès !");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer l'utilisateur : il existe des réservations ou d'autres relations associées. Veuillez d'abord supprimer ces relations.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("editMode", true);
        model.addAttribute("createMode", true);
        return "admin/user-details";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String createUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        try {
            User savedUser = userUseCase.createUser(user);
            redirectAttributes.addFlashAttribute("success", "Utilisateur créé avec succès !");
            return "redirect:/admin/users/" + savedUser.getIdUser();
        } catch (IllegalArgumentException e) {
            // En cas d'erreur de validation, on reste sur la page de création
            model.addAttribute("user", user);
            model.addAttribute("editMode", true);
            model.addAttribute("createMode", true);
            model.addAttribute("error", e.getMessage());
            return "admin/user-details";
        } catch (Exception e) {
            // Autres erreurs
            model.addAttribute("user", user);
            model.addAttribute("editMode", true);
            model.addAttribute("createMode", true);
            model.addAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "admin/user-details";
        }
    }
}