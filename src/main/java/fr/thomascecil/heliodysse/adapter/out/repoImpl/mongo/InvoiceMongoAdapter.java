package fr.thomascecil.heliodysse.adapter.out.repoImpl.mongo;

import fr.thomascecil.heliodysse.adapter.out.mapper.InvoiceMapper;
import fr.thomascecil.heliodysse.adapter.out.repository.mongo.MongoInvoiceRepository;
import fr.thomascecil.heliodysse.domain.model.entity.Invoice;
import fr.thomascecil.heliodysse.domain.port.out.mongo.InvoicePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InvoiceMongoAdapter implements InvoicePort {

    private final MongoInvoiceRepository repository;
    private final InvoiceMapper mapper;

    public InvoiceMongoAdapter(MongoInvoiceRepository repository, InvoiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveInvoiceFromBooking(Invoice invoice) {
        System.out.println("💾 Enregistrement dans Mongo : " + invoice.getInvoiceNumber());
        repository.save(mapper.toEntity(invoice));
    }

    @Override
    public Optional<Invoice> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }


    @Override
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return repository.findByInvoiceNumber(invoiceNumber)
                .map(mapper::toDomain);
    }

    @Override
    public List<Invoice> findInvoicesByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findAllInvoices() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
