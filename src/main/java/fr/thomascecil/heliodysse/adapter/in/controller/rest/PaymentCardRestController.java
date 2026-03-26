package fr.thomascecil.heliodysse.adapter.in.controller.rest;

import fr.thomascecil.heliodysse.adapter.in.dto.mapper.PaymentCardDTOMapper;
import fr.thomascecil.heliodysse.adapter.in.dto.request.paymentCard.PaymentCardCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.paymentCard.PaymentCardResponseDTO;
import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsImpl;
import fr.thomascecil.heliodysse.domain.model.entity.PaymentCard;
import fr.thomascecil.heliodysse.domain.port.in.PaymentCardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paymentCard")
public class PaymentCardRestController {

    private final PaymentCardUseCase paymentCardUseCase;
    private final PaymentCardDTOMapper mapper;

    public PaymentCardRestController(PaymentCardUseCase paymentCardUseCaseUseCase, PaymentCardDTOMapper mapper) {
        this.paymentCardUseCase = paymentCardUseCaseUseCase;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<PaymentCard> getPaymentCardById(@PathVariable Long id) {
        return paymentCardUseCase.getPaymentCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PaymentCard>> getAllPaymentCard() {
        List<PaymentCard> paymentCards = paymentCardUseCase.getAll();
        return ResponseEntity.ok(paymentCards);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PaymentCard>> getUsersPaymentCards(@PathVariable Long id) {
        List<PaymentCard> paymentCards = paymentCardUseCase.getPaymentCardsByClient(id);
        return ResponseEntity.ok(paymentCards);
    }

    @GetMapping("/default/{id}")
    public ResponseEntity<PaymentCard> getDefaultPaymentCardById(@PathVariable Long id) {
        return paymentCardUseCase.getDefaultPaymentCardByClient(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentCardResponseDTO> createPaymentCard (@AuthenticationPrincipal UserDetailsImpl userDetails , @RequestBody PaymentCardCreateDTO paymentCardCreateDTO){
        //ajouter la carte de payment dans les paramètres
        PaymentCard createdPaymentCard = paymentCardUseCase.createPaymentCard(userDetails.getUser().getIdUser(), paymentCardCreateDTO.getPaymentMethodId());
        PaymentCardResponseDTO responseDTO = mapper.toDto(createdPaymentCard);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/default/set/{id}")
    public ResponseEntity<PaymentCardResponseDTO> setDefaultCard(@PathVariable Long id){
        paymentCardUseCase.setDefaultCard(id);
        return ResponseEntity.noContent().build();
    }

    //user auth
    @GetMapping("/me")
    public ResponseEntity<List<PaymentCardResponseDTO>> getMyCards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getIdUser();
        List<PaymentCard> cards = paymentCardUseCase.getPaymentCardsByClient(userId);

        List<PaymentCardResponseDTO> responseDTO = cards.stream()
                .map(mapper::toDto) // ← transforme chaque carte en DTO
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyCard(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        paymentCardUseCase.deleteUserAuthCardById(userDetails.getUser().getIdUser(), id);
        return ResponseEntity.noContent().build();
    }
}
