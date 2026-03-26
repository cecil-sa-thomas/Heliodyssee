package fr.thomascecil.heliodysse.domain.port.out.view;

import fr.thomascecil.heliodysse.domain.model.entity.viewdto.BookingInfo;

import java.util.List;

public interface UserBookingsRepository {

    List<BookingInfo> findBookingsWithDetailsByUserId(Long Id);
}
