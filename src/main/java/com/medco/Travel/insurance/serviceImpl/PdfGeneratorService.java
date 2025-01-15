package com.medco.Travel.insurance.serviceImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.medco.Travel.insurance.entity.Passenger;
import com.medco.Travel.insurance.entity.Policy;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class PdfGeneratorService {
    public String generateCertificate(Passenger passenger, Policy policy) {
        String filePath = "certificates/" + policy.getPolicyId() + ".pdf";
        try (OutputStream os = new FileOutputStream(filePath)) {
            Document document = new Document();
            PdfWriter.getInstance(document, os);
            document.open();

            // Add certificate details
            document.add(new Paragraph("Travel Insurance Certificate"));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph("Passenger Name: " + passenger.getFirstName() + " " + passenger.getLastName()));
            document.add(new Paragraph("Passport Number: " + passenger.getPassportNumber()));
            document.add(new Paragraph("Telephone: " + passenger.getTelephone()));
            document.add(new Paragraph("Destination: " + passenger.getDestination().getCountryName()));
            document.add(new Paragraph("Insurance Policy ID: " + policy.getPolicyId()));
            document.add(new Paragraph("Premium: $" + policy.getPremiumAmount()));
            document.add(new Paragraph("Coverage Start Date: " + policy.getStartDate()));
            document.add(new Paragraph("Coverage End Date: " + policy.getEndDate()));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
