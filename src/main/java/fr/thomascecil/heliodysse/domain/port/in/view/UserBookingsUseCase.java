package fr.thomascecil.heliodysse.domain.port.in.view;

import fr.thomascecil.heliodysse.domain.model.entity.viewdto.BookingInfo;

import java.util.List;

public interface UserBookingsUseCase {
    List<BookingInfo> getUpcomingFlightsWithDetails(Long userId);
}
