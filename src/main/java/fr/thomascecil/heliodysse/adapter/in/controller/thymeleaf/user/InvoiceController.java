package fr.thomascecil.heliodysse.adapter.in.controller.thymeleaf.user;

import fr.thomascecil.heliodysse.adapter.out.pdf.PdfGenerator;
import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsImpl;
import fr.thomascecil.heliodysse.domain.model.entity.Invoice;
import fr.thomascecil.heliodysse.domain.port.in.InvoiceUseCase;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;

    public InvoiceController(InvoiceUseCase invoiceUseCase) {
        this.invoiceUseCase = invoiceUseCase;
    }

    @GetMapping("/invoices")
    public String getUserInvoices(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        Long userId = userDetails.getUser().getIdUser();
        List<Invoice> invoices = invoiceUseCase.getInvoicesByUserId(userId);
        model.addAttribute("invoices", invoices);
        return "/fragments/session/user-menu/invoices-list";
    }

    @GetMapping("/invoices/{id}/pdf")
    public void downloadInvoicePdf(@PathVariable String id, HttpServletResponse response) {
        invoiceUseCase.getInvoiceById(id).ifPresentOrElse(invoice -> {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf");
            try {
                PdfGenerator.generateInvoicePdf(invoice, response.getOutputStream());
            } catch (Exception e) {
                throw new RuntimeException("PDF generation failed", e);
            }
        }, () -> {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        });
    }
}
