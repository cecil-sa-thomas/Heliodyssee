package fr.thomascecil.heliodysse.adapter.out.repoImpl.view;

import fr.thomascecil.heliodysse.adapter.out.repository.view.JpaUserBookingsRepository;
import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import fr.thomascecil.heliodysse.domain.model.entity.viewdto.BookingInfo;
import fr.thomascecil.heliodysse.domain.port.out.view.UserBookingsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserBookingsRepositoryImpl implements UserBookingsRepository {

    private final JpaUserBookingsRepository jpaRepo;

    public UserBookingsRepositoryImpl(JpaUserBookingsRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public List<BookingInfo> findBookingsWithDetailsByUserId(Long id) {
        return jpaRepo.findAllByUserIdWithFlightDetails(id);
    }
}
