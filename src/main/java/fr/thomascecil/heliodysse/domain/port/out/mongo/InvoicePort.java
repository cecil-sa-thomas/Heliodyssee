package fr.thomascecil.heliodysse.domain.port.out.mongo;

import fr.thomascecil.heliodysse.domain.model.entity.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoicePort {

    //Booking Repo
    void saveInvoiceFromBooking(Invoice invoice);

    Optional<Invoice> findById(String id); // ID Mongo

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber); // UUID unique

    List<Invoice> findInvoicesByUserId(Long userId); // ID SQL du User

    List<Invoice> findAllInvoices();

}
