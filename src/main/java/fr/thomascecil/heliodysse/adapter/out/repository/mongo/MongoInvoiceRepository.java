package fr.thomascecil.heliodysse.adapter.out.repository.mongo;

import fr.thomascecil.heliodysse.adapter.out.mongoEntity.InvoiceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoInvoiceRepository extends MongoRepository<InvoiceDocument, String> {
    List<InvoiceDocument> findByUserId(Long userId);// Pour GET MINE
    List<InvoiceDocument> findByUserEmail(String email);
    Optional<InvoiceDocument> findByInvoiceNumber(String invoiceNumber);

}
