package fr.thomascecil.heliodysse.adapter.out.jpaEntity;

import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "booking") // <-- correction ici !
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_booking")
    private Long idBooking;

    @Column(name = "seat_number", nullable = false) // <-- correction ici aussi !
    private short seatNumber;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "first_name_passenger", nullable = false, length = 50)
    private String firstNamePassenger; // <-- correction ici !

    @Column(name = "last_name_passenger", nullable = false, length = 50)
    private String lastNamePassenger;

    @Column(name = "number_passenger", nullable = false, length = 50)
    private String numberPassenger;

    @Column(name = "passenger_age", nullable = false)
    private short passengerAge;

    @Column(name = "gender", nullable = false)
    private boolean gender;

    @Enumerated(EnumType.STRING) // <-- important pour enum
    @Column(name = "status", nullable = false, length = 50)
    private BookingStatus status;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Column(name = "last_modification_by", length = 50)
    private String lastModificationBy;

    @Version
    @Column(name = "version", nullable = false)
    private Short version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_flight")
    private FlightEntity flight;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user")
    private UserEntity user;
}
