package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.base.GenericService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import java.util.List;

public interface PaymentService extends GenericService<Payment> {
    List<Payment> getPaymentsWithPendingApprovalInvoices();

    List<Payment> getAllPaymentsOfParticularInvoice(String invoiceId);

    static void generateReceipt(Payment payment) {
        try {
            String path = "E:\\Pahappa Documents\\automated-invoicing\\Receipt.pdf";
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            document.setMargins(36, 36, 20, 36);


            String imagePath = "E:\\Pahappa Documents\\automated-invoicing\\pahappa_limited_logo.jpeg";
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.setFixedPosition(80, 670); // Set the position of the image
            document.add(image);

            Paragraph title = new Paragraph("Receipt")
                    .setBold()
                    .setFontSize(28f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10f);
            // .setFontColor(Color.BLUE);
            document.add(title);

            // Receipt Details
            document.add(new Paragraph()
                    .add("Receipt: "+payment.getInvoice().getInvoiceNumber())
                    .add("\nReceipt Date: " + LocalDate.now())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginBottom(20f));

            // A table for the receipt details section
            Table detailsTable = new Table(1);
            detailsTable.setWidth(300);
            detailsTable.setMarginLeft(80);
            detailsTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

            // Company details (name, email, phone, address)
            detailsTable.addCell(new Cell().add("Pahappa Limited").setBorder(Border.NO_BORDER).setFontSize(20f).setBold());
            detailsTable.addCell(new Cell().add("info@pahappa.com").setBorder(Border.NO_BORDER));
            detailsTable.addCell(new Cell().add("+256 774733493/+256 7040733492").setBorder(Border.NO_BORDER));
            detailsTable.addCell(new Cell().add("Plot No. 153 Muteesa II Road, Ntinda").setBorder(Border.NO_BORDER));

            document.add(detailsTable);


            // Customer Information table
            Table customerTable = new Table(new float[]{2, 3});
            customerTable.setWidthPercent(70);

            customerTable.addCell(createCell("Customer Name:", TextAlignment.LEFT, true));
            customerTable.addCell(createCell(payment.getInvoice().getClientSubscription().getClient().getClientFirstName()+" "+payment.getInvoice().getClientSubscription().getClient().getClientLastName(), TextAlignment.LEFT, false));

            customerTable.addCell(createCell("Contact:", TextAlignment.LEFT, true));
            customerTable.addCell(createCell(payment.getInvoice().getClientSubscription().getClient().getClientContact(), TextAlignment.LEFT, false));
            customerTable.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(customerTable);

            // Payment Information
            document.add(new Paragraph("\nPayment Details:")
                    .setBold()
                    .setFontSize(16f)
                    // .setFontColor(Color.BLUE)
                    .setMarginLeft(80));

            Table paymentTable = new Table(new float[]{2, 3});
            paymentTable.setWidthPercent(70);

            paymentTable.addCell(createCell("Amount Paid:", TextAlignment.LEFT, true));
            paymentTable.addCell(createCell(String.valueOf(payment.getAmountPaid()), TextAlignment.LEFT, false));

            paymentTable.addCell(createCell("Payment Method:", TextAlignment.LEFT, true));
            paymentTable.addCell(createCell(String.valueOf(payment.getPaymentMethod()), TextAlignment.LEFT, false));

            paymentTable.addCell(createCell("Balance Due:", TextAlignment.LEFT, true));
            double balanceDue= payment.getInvoice().getInvoiceBalance()-payment.getAmountPaid();
            System.out.println("Invoice balance is :"+payment.getInvoice().getInvoiceTotalAmount());
            System.out.println("Amount Paid is :"+payment.getAmountPaid());
            paymentTable.addCell(createCell(String.valueOf(balanceDue), TextAlignment.LEFT, false));

            paymentTable.addCell(createCell("Being Paid For:", TextAlignment.LEFT, true));
            paymentTable.addCell(createCell(payment.getInvoice().getClientSubscription().getSubscription().getProduct().getProductName(), TextAlignment.LEFT, false));
            paymentTable.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(paymentTable);

            //copyright
            Paragraph copyright = new Paragraph("Copyright © "+ Year.now().getValue()+" Pahappa Limited. All rights reserved.")
                    .setFontSize(8)
                    .setFontColor(Color.BLUE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10);

            document.add(copyright);

            document.close();
            System.out.println("Receipt created Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static Cell createCell(String content, TextAlignment alignment, boolean isBold) {
        Paragraph paragraph = new Paragraph(content);
        if (isBold) {
            paragraph.setBold();
        }
        return new Cell().add(paragraph).setTextAlignment(alignment).setPadding(5);
    }

}
