package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf.admin;

import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import fr.thomascecil.heliodysse.domain.port.in.BookingUseCase;
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
@RequestMapping("/admin/bookings")
public class BookingsController {

    @Autowired
    private BookingUseCase bookingUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public String showBookingList(Model model) {
        List<Booking> bookings = bookingUseCase.getAll();
        model.addAttribute("bookings", bookings);
        return "admin/bookings";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public String showBookingDetail(@PathVariable Long id, @RequestParam(value = "edit", required = false) Boolean edit, Model model) {
        Booking booking = bookingUseCase.getBookingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Réservation non trouvée"));
        model.addAttribute("booking", booking);
        model.addAttribute("editMode", Boolean.TRUE.equals(edit));
        return "admin/booking-details";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/edit")
    public String updateBooking(@PathVariable Long id, @ModelAttribute Booking booking, Model model, RedirectAttributes redirectAttributes) {
        try {
            booking.setIdBooking(id); // pour être sûr que l'ID reste correct
            bookingUseCase.updateBooking(booking);
            redirectAttributes.addFlashAttribute("success", "Réservation mise à jour !");
            return "redirect:/admin/bookings/" + id;
        } catch (IllegalArgumentException e) {
            // En cas d'erreur de validation, on reste sur la page d'édition
            model.addAttribute("booking", booking);
            model.addAttribute("editMode", true);
            model.addAttribute("error", e.getMessage());
            return "admin/booking-details";
        } catch (Exception e) {
            // Autres erreurs
            model.addAttribute("booking", booking);
            model.addAttribute("editMode", true);
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "admin/booking-details";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingUseCase.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Réservation supprimée avec succès !");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer la réservation : il existe des relations associées. Veuillez d'abord supprimer ces relations.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("editMode", true);
        model.addAttribute("createMode", true);
        return "admin/booking-details";
    }

}