package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf.admin;

import fr.thomascecil.heliodysse.domain.model.entity.Flight;
import fr.thomascecil.heliodysse.domain.port.in.FlightUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/flights")
public class FlightsController {

    @Autowired
    private FlightUseCase flightUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public String showFlightList(Model model) {
        List<Flight> flights = flightUseCase.getAll();
        model.addAttribute("flights", flights);
        return "admin/flights";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String showFlightDetail(@PathVariable Long id, @RequestParam(value = "edit", required = false) Boolean edit, Model model) {
        Flight flight = flightUseCase.getFlightById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vol non trouvé"));
        model.addAttribute("flight", flight);
        model.addAttribute("editMode", Boolean.TRUE.equals(edit));
        return "admin/flight-details";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String updateFlight(@PathVariable Long id, @ModelAttribute Flight flight, Model model, RedirectAttributes redirectAttributes) {
        try {
            flight.setIdFlight(id); // pour être sûr que l'ID reste correct
            flightUseCase.updateFlight(flight);
            redirectAttributes.addFlashAttribute("success", "Vol mis à jour !");
            return "redirect:/admin/flights/" + id;
        } catch (IllegalArgumentException e) {
            // En cas d'erreur de validation, on reste sur la page d'édition
            model.addAttribute("flight", flight);
            model.addAttribute("editMode", true);
            model.addAttribute("error", e.getMessage());
            return "admin/flight-details";
        } catch (Exception e) {
            // Autres erreurs
            model.addAttribute("flight", flight);
            model.addAttribute("editMode", true);
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "admin/flight-details";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteFlight(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            flightUseCase.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Vol supprimé avec succès !");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer le vol : il existe des réservations ou d'autres relations associées. Veuillez d'abord supprimer ces relations.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/flights";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("flight", new Flight());
        model.addAttribute("editMode", true);
        model.addAttribute("createMode", true);
        return "admin/flight-details";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String createFlight(@ModelAttribute Flight flight, Model model, RedirectAttributes redirectAttributes) {
        try {
            Flight savedFlight = flightUseCase.createFlight(flight);
            redirectAttributes.addFlashAttribute("success", "Vol créé avec succès !");
            return "redirect:/admin/flights/" + savedFlight.getIdFlight();
        } catch (IllegalArgumentException e) {
            // En cas d'erreur de validation, on reste sur la page de création
            model.addAttribute("flight", flight);
            model.addAttribute("editMode", true);
            model.addAttribute("createMode", true);
            model.addAttribute("error", e.getMessage());
            return "admin/flight-details";
        } catch (Exception e) {
            // Autres erreurs
            model.addAttribute("flight", flight);
            model.addAttribute("editMode", true);
            model.addAttribute("createMode", true);
            model.addAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "admin/flight-details";
        }
    }
}