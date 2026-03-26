package fr.thomascecil.heliodysse.adapter.in.controller.rest.view;

import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsImpl;
import fr.thomascecil.heliodysse.domain.model.entity.viewdto.BookingInfo;
import fr.thomascecil.heliodysse.domain.port.in.view.UserBookingsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class UserBookingRestController {

    private final UserBookingsUseCase userBookingsUseCase;

    public UserBookingRestController(UserBookingsUseCase userBookingsUseCase) {
        this.userBookingsUseCase = userBookingsUseCase;
    }

    // 📦 GET toutes les réservations à venir d’un utilisateur
    @GetMapping("/upcoming/user")
    public ResponseEntity<List<BookingInfo>> getUpcomingBookingsByUserId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BookingInfo> result = userBookingsUseCase.getUpcomingFlightsWithDetails(userDetails.getUser().getIdUser());
        return ResponseEntity.ok(result);
    }
}
