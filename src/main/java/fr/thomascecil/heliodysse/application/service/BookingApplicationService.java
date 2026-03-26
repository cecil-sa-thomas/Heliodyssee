package fr.thomascecil.heliodysse.application.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Plan;
import com.stripe.param.PaymentIntentCreateParams;
import fr.thomascecil.heliodysse.domain.model.entity.*;
import fr.thomascecil.heliodysse.domain.model.enums.bookingEnum.BookingStatus;
import fr.thomascecil.heliodysse.domain.port.in.BookingUseCase;
import fr.thomascecil.heliodysse.domain.port.out.*;
import fr.thomascecil.heliodysse.domain.port.out.mongo.InvoicePort;
import fr.thomascecil.heliodysse.domain.service.BookingService;
import fr.thomascecil.heliodysse.domain.service.FlightService;
import fr.thomascecil.heliodysse.domain.service.payment.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingApplicationService implements BookingUseCase {
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final FlightRepository flightRepository;
    private final FlightService flightService;
    private final UserRepository userRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final PlanetRepository planetRepository;
    private final SpacePortRepository spacePortRepository;
    private final InvoicePort invoicePort;
    private final InvoiceService invoiceService;


    public BookingApplicationService(BookingRepository bookingRepository, UserRepository userRepository, FlightRepository flightRepository, UserRepository userRepository1, PaymentCardRepository paymentCardRepository, PlanetRepository planetRepository, SpacePortRepository spacePortRepository, InvoicePort invoicePort) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository1;
        this.paymentCardRepository = paymentCardRepository;
        this.planetRepository = planetRepository;
        this.spacePortRepository = spacePortRepository;
        this.invoicePort = invoicePort;
        this.flightService = new FlightService();
        this.bookingService = new BookingService();
        this.invoiceService = new InvoiceService();
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getByUserId(Long idUser) {
        return bookingRepository.findByUserId(idUser);
    }

    @Override
    public List<Booking> getByFlightId(Long idFlight) {
        return bookingRepository.findByFlightId(idFlight);
    }

    @Override
    public List<Booking> getByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<Booking> getByDateCreation(LocalDateTime dateCreation) {
        return List.of();
    }

    @Override
    public List<Booking> getByDateCreationAfter(LocalDateTime date) {
        return List.of();
    }

    @Override
    public List<Booking> getByDateCreationBefore(LocalDateTime date) {
        return List.of();
    }

    @Override
    public List<Booking> getByDateCreationBetween(LocalDateTime start, LocalDateTime end) {
        return List.of();
    }

    @Override
    public List<Booking> getByPrice(BigDecimal price) {
        return List.of();
    }

    @Override
    public List<Booking> getAll() {

        return bookingRepository.findAll();
    }


    @Transactional
    @Override
    public Booking createBookingWithPayment(Booking booking, String stripeId, Long clientId) {
        // 1. Récupérer le vol
        Flight flightFound = flightRepository.findById(booking.getIdFlight())
                .orElseThrow(() -> new EntityNotFoundException("Vol introuvable."));

        // 2. Assigner un numéro de siège libre
        List<Booking> existingBookings = bookingRepository.findByFlightId(flightFound.getIdFlight());
        booking.setSeatNumber(bookingService.findFreeSeatNumber(existingBookings));

        // 3. Vérifier que le vol est encore réservable
        flightService.decrementSeatsAvailable(flightFound);
        flightRepository.save(flightFound);

        // 4. Générer un numéro passager unique
        String numberPassenger = bookingService.setNumberPassenger(
                booking.getIdFlight(),
                booking.getLastNamePassenger(),
                booking.getFirstNamePassenger(),
                booking.getPassengerAge(),
                booking.getIdUser(),
                booking.getGender()
        );
        booking.setNumberPassenger(numberPassenger);

        // 5. Fixer le prix (depuis le vol)
        booking.setPrice(flightFound.getPrice());

        // 6. Paiement Stripe
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable."));

        booking.setIdUser(client.getIdUser());//ajout pour correction de bug

        if (client.getStripeCustomerId() == null) {
            throw new IllegalStateException("Le client n'a pas de Stripe Customer ID.");
        }

        // 6.1. Récupérer la carte à utiliser
        PaymentCard selectedCard = paymentCardRepository.findByUserId(clientId).stream()
                .filter(card -> card.getStripeId().equals(stripeId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Carte de paiement non trouvée."));

        // 6.2. Créer l’intention de paiement
        try {
            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount(booking.getPrice().multiply(BigDecimal.valueOf(100)).longValue())// montant en centimes avec méthode BigDecimal
                    .setCurrency("eur")
                    .setCustomer(client.getStripeCustomerId())
                    .setPaymentMethod(selectedCard.getStripeId()) // déja un pm_xxx
                    .setConfirm(true) // as de redirection
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC)
                    .setReturnUrl("http://localhost:8080/confirmation") //redirection
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            if (!"succeeded".equals(paymentIntent.getStatus())) {
                throw new RuntimeException("Paiement échoué : " + paymentIntent.getStatus());
            }
        } catch (StripeException e) {
            throw new RuntimeException("Erreur Stripe : " + e.getMessage(), e);
        }

        // 7. Validation métier
        bookingService.validateBooking(booking);

        // 8. Génération et sauvegarde de la facture
        SpacePort departurePort = spacePortRepository.findById(flightFound.getIdDeparture())
                .orElseThrow(() -> new EntityNotFoundException("Port de départ introuvable."));
        SpacePort arrivalPort = spacePortRepository.findById(flightFound.getIdArrival())
                .orElseThrow(() -> new EntityNotFoundException("Port d'arrivée introuvable."));

        Planet departurePlanet = planetRepository.findById(departurePort.getIdPlanet())
                .orElseThrow(() -> new EntityNotFoundException("Planète de départ introuvable."));
        Planet arrivalPlanet = planetRepository.findById(arrivalPort.getIdPlanet())
                .orElseThrow(() -> new EntityNotFoundException("Planète d'arrivée introuvable."));


        Invoice invoice = invoiceService.generateFromBooking(
                booking, flightFound, client, selectedCard,
                departurePort, arrivalPort, departurePlanet, arrivalPlanet
        );
        System.out.println("✅ Facture générée : " + invoice.getInvoiceNumber());
        invoicePort.saveInvoiceFromBooking(invoice);
        System.out.println("📄 Facture complète : " + invoice);
        // 9. Sauvegarde finale de la réservation
        return bookingRepository.save(booking);

    }


    @Override
    public Booking updateBooking(Booking booking) {
        Booking found = bookingRepository.findById(booking.getIdBooking())
                .orElseThrow(() -> new EntityNotFoundException("booking introuvable"));
        bookingService.updateBookingFromBooking(booking, found);
        bookingService.updateInfoSystem(found);
        bookingService.validateBooking(found);
        return bookingRepository.save(found);
    }


    @Override
    public void deleteById(Long id) {

        if (!bookingRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("No booking found with id : " + id);
        }
        bookingRepository.deleteById(id);
    }
}
