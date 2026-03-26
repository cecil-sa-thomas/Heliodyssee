package fr.thomascecil.heliodysse.application.service.view;

import fr.thomascecil.heliodysse.domain.model.entity.viewdto.BookingInfo;
import fr.thomascecil.heliodysse.domain.port.in.view.UserBookingsUseCase;
import fr.thomascecil.heliodysse.domain.port.out.view.UserBookingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBookingsApplicationService implements UserBookingsUseCase {

    private final UserBookingsRepository userBookingsRepository;

    public UserBookingsApplicationService(UserBookingsRepository userBookingsRepository) {
        this.userBookingsRepository = userBookingsRepository;
    }

    @Override
    public List<BookingInfo> getUpcomingFlightsWithDetails(Long userId) {
        return userBookingsRepository.findBookingsWithDetailsByUserId(userId).stream()
                .filter(b -> b.dateDeparture().isAfter(java.time.LocalDateTime.now()))
                .toList();
    }

}
