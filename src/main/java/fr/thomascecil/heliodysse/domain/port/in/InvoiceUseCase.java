package fr.thomascecil.heliodysse.domain.port.in;

import fr.thomascecil.heliodysse.domain.model.entity.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceUseCase {
    List<Invoice> getInvoicesByUserId(Long userId);
    Optional<Invoice> getInvoiceById(String id);
}
