package fr.thomascecil.heliodysse.adapter.in.controller.rest;

import fr.thomascecil.heliodysse.adapter.in.dto.mapper.BookingDtoMapper;
import fr.thomascecil.heliodysse.adapter.in.dto.request.booking.BookingCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.booking.BookingUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.booking.BookingResponseDTO;
import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsImpl;
import fr.thomascecil.heliodysse.domain.model.entity.Booking;
import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import fr.thomascecil.heliodysse.domain.port.in.BookingUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingRestController {
    private final BookingUseCase bookingUseCase;
    private final BookingDtoMapper mapper;

    public BookingRestController(BookingUseCase bookingUseCase, BookingDtoMapper mapper) {
        this.bookingUseCase = bookingUseCase;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingUseCase.getBookingById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{idUser}")
    public List<BookingResponseDTO> getBookingByUserId(@PathVariable Long idUser){
        List<Booking> bookings = bookingUseCase.getByUserId(idUser);
        List<BookingResponseDTO> bookingResponseDTO = bookings.stream().map(mapper::toDto).collect(Collectors.toList());
        return bookingResponseDTO;
    }


    @GetMapping("/flight/{idFlight}")
    public List<BookingResponseDTO> getBookingByFlightId(@PathVariable Long idFlight){
        List<Booking> bookings = bookingUseCase.getByFlightId(idFlight);
        List<BookingResponseDTO> bookingResponseDTO = bookings.stream().map(mapper::toDto).collect(Collectors.toList());
        return bookingResponseDTO;
    }

    @GetMapping("/status/{status}")
    public List<BookingResponseDTO> getBookingByStatus(@PathVariable BookingStatus status){
        List<Booking> bookings = bookingUseCase.getByStatus(status);
        List<BookingResponseDTO> bookingResponseDTO = bookings.stream().map(mapper::toDto).collect(Collectors.toList());
        return bookingResponseDTO;
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable Long id, @RequestBody BookingUpdateDTO bookingUpdateDTO) {
        // Récupération et mise à jour - TODO: refactoriser vers le use case
        Booking booking = bookingUseCase.getBookingById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        mapper.updateBookingFromDto(bookingUpdateDTO, booking);
        Booking updated = bookingUseCase.updateBooking(booking);
        BookingResponseDTO updatedDto = mapper.toDto(booking);
        return ResponseEntity.ok(updatedDto);
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBookingWithPayment(@RequestBody BookingCreateDTO bookingCreateDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //ajouter la carte de payment dans les paramètres
        Booking booking = mapper.toDomain(bookingCreateDTO);
        String stripeId = bookingCreateDTO.getStripeId();
        Long clientId = userDetails.getUser().getIdUser();
        System.out.println("DEBUG --- booking.getIdFlight() = " + booking.getIdFlight());
        Booking createdBooking = bookingUseCase.createBookingWithPayment(booking, stripeId, clientId);
        BookingResponseDTO responseDTO = mapper.toDto(createdBooking);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public List<BookingResponseDTO> getAllBookings(){
        List<Booking> bookings = bookingUseCase.getAll();
        List<BookingResponseDTO> bookingResponseDTO = bookings.stream().map(mapper::toDto).collect(Collectors.toList());
        return bookingResponseDTO;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookings(@PathVariable Long id) {
        bookingUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
/*

    List<Booking> getByStatus(BookingStatus status);
*/

}


//GetBooking Avec les Info du vol
//  -> usecase du booking pour les info du booking
//  -> usecase du booking.getvoldId pour les info du vol
//  -> construction du dto BookingResponseDto