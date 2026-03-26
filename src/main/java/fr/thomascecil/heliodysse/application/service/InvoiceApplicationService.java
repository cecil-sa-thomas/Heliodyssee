package fr.thomascecil.heliodysse.application.service;


import fr.thomascecil.heliodysse.domain.model.entity.Invoice;
import fr.thomascecil.heliodysse.domain.port.in.InvoiceUseCase;
import fr.thomascecil.heliodysse.domain.port.out.mongo.InvoicePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class InvoiceApplicationService implements InvoiceUseCase {
    private final InvoicePort invoicePort;


    public InvoiceApplicationService(InvoicePort invoicePort) {
        this.invoicePort = invoicePort;
    }


    @Override
    public List<Invoice> getInvoicesByUserId(Long userId) {
        return invoicePort.findInvoicesByUserId(userId);
    }

    @Override
    public Optional<Invoice> getInvoiceById(String id) {
        return invoicePort.findById(id);
    }
}
