package fr.thomascecil.heliodysse.adapter.out.pdf;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import fr.thomascecil.heliodysse.domain.model.entity.Invoice;

import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {

    public static void generateInvoicePdf(Invoice invoice, OutputStream outputStream) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        document.add(new Paragraph("Facture n°: " + invoice.getInvoiceNumber(), titleFont));
        document.add(Chunk.NEWLINE);

        // === Passager ===
        document.add(new Paragraph("Informations du passager", sectionFont));
        document.add(new Paragraph("Nom: " + invoice.getLastNamePassenger(), normalFont));
        document.add(new Paragraph("Prénom: " + invoice.getFirstNamePassenger(), normalFont));
        document.add(new Paragraph("Âge: " + invoice.getPassengerAge(), normalFont));
        document.add(new Paragraph("Sexe: " + (Boolean.TRUE.equals(invoice.getGender()) ? "Homme" : "Femme"), normalFont));
        document.add(new Paragraph("N° Passager: " + invoice.getNumberPassenger(), normalFont));
        document.add(Chunk.NEWLINE);

        // === Vol ===
        document.add(new Paragraph("Détails du vol", sectionFont));
        document.add(new Paragraph("N° Vol: " + invoice.getFlightNumber(), normalFont));
        document.add(new Paragraph("Départ: " + invoice.getDeparturePlanet() + " - " + invoice.getDeparturePort(), normalFont));
        document.add(new Paragraph("Arrivée: " + invoice.getArrivalPlanet() + " - " + invoice.getArrivalPort(), normalFont));
        document.add(new Paragraph("Date Départ: " + formatter.format(invoice.getDepartureTime()), normalFont));
        document.add(new Paragraph("Date Arrivée: " + formatter.format(invoice.getArrivalTime()), normalFont));
        document.add(Chunk.NEWLINE);

        // === Paiement ===
        document.add(new Paragraph("Paiement", sectionFont));
        document.add(new Paragraph("Montant: " + invoice.getPrice() + " " + invoice.getCurrency(), normalFont));
        document.add(new Paragraph("Carte: " + invoice.getCardBrand() + " **** " + invoice.getCardLastDigits(), normalFont));
        document.add(new Paragraph("Expiration: " + invoice.getCardExpMonth() + "/" + invoice.getCardExpYear(), normalFont));
        document.add(Chunk.NEWLINE);

        // === Meta ===
        document.add(new Paragraph("Informations supplémentaires", sectionFont));
        document.add(new Paragraph("Email acheteur: " + invoice.getUserEmail(), normalFont));
        document.add(new Paragraph("Date Réservation: " + formatter.format(invoice.getBookingDate()), normalFont));
        document.add(new Paragraph("Date Facture: " + formatter.format(invoice.getInvoiceDate()), normalFont));
        document.add(new Paragraph("Statut: " + invoice.getBookingStatus(), normalFont));
        document.add(new Paragraph("CGV: " + invoice.getTermsAndConditionsVersion(), normalFont));

        document.close();
    }
}

