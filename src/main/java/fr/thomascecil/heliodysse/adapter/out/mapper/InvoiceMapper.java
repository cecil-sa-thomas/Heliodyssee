package fr.thomascecil.heliodysse.adapter.out.mapper;

import fr.thomascecil.heliodysse.adapter.out.mongoEntity.InvoiceDocument;
import fr.thomascecil.heliodysse.domain.model.entity.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice toDomain(InvoiceDocument document);
    InvoiceDocument toEntity(Invoice invoice);
}
