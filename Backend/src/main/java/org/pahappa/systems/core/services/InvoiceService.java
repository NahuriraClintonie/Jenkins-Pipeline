package org.pahappa.systems.core.services;
//imports
import com.googlecode.genericdao.search.Search;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface InvoiceService extends GenericService<Invoice> {

    void changeStatusToPendingApproval(Invoice invoice);
    void changeStatusToPaid(Invoice invoice, double amount);

    void changeStatusToPartiallyPaid(Invoice invoice, double amount) throws ValidationFailedException, OperationFailedException;

    static void generateInvoicePdf(Invoice invoice, PaymentTerms paymentTerms){
        try{
            String path = "/home/devclinton/Documents/Pahappa/automated-invoicing/automated-invoicing/Invoice.pdf";
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            String imagePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/automated-invoicing/pahappaLogo1.jpg";
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);

            String imagePath1 ="/home/devclinton/Documents/Pahappa/automated-invoicing/automated-invoicing/pahappaLogo2.jpg";
            ImageData imageData1 = ImageDataFactory.create(imagePath1);
            Image image1 = new Image(imageData1);
            float x = pdfDocument.getDefaultPageSize().getWidth()/3;
            float y = pdfDocument.getDefaultPageSize().getHeight()/3;
            image1.setFixedPosition(x, 400);
            image1.setOpacity(0.1f);
            image.setFixedPosition(350, 650);
            document.add(image);
            document.add(image1);

            // creating an invoice
            float thirdColumn = 190f;
            float secondColumn = 285f;
            float firstColumn = secondColumn+150f;
            float[] columnWidth = {firstColumn,secondColumn};
            // float[] oneColumn = {firstColumn};
            float[] fullWidth = {3*thirdColumn};
            float[] sixColWidth = {thirdColumn,thirdColumn,thirdColumn,thirdColumn,thirdColumn,thirdColumn};
            Paragraph space = new Paragraph("\n");

            Paragraph title = new Paragraph("Invoice")
                    .setBold()
                    .setFontSize(28f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10f);
            // .setFontColor(Color.BLUE);
            document.add(title);

            // creating the table
            Table table = new Table(columnWidth);
            table.addCell(new Cell().add("").setFontSize(20f).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(paymentTerms.getCompanyName()).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(paymentTerms.getEmail()).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(paymentTerms.getAddress()).setBorder(Border.NO_BORDER));
            Table nestedTable = new Table(new float[]{secondColumn/2,secondColumn/2});
//            nestedTable.addCell(getHeaderTextCell("Invoice No: "));
            nestedTable.addCell(new Cell().add("Invoice No: ").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
//            nestedTable.addCell(getHeaderTextValue(invoice.getInvoiceNumber().toString()));
            nestedTable.addCell(new Cell().add(invoice.getInvoiceNumber()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//            nestedTable.addCell(getHeaderTextCell("Invoice Date: "));
            nestedTable.addCell(new Cell().add("Date: ").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
//            nestedTable.addCell(getHeaderTextValue("15/11/2023"));
            nestedTable.addCell(new Cell().add(((String.valueOf(LocalDate.now())))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

            Border bluBorder = new SolidBorder(com.itextpdf.kernel.color.Color.BLUE, 2f);
            Table dividerTable = new Table(fullWidth);
            dividerTable.setBorder(bluBorder);


            document.add(table);
            document.add(space);
            document.add(dividerTable);
            // document.add(space);

            Table billingTable = new Table(columnWidth);
            // billingTable.addCell(getCell10fLeft("Client's Name",true));

            billingTable.addCell(new Cell().add("Bill To: ").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//            billingTable.addCell(getCell10fLeft("Bill To: ",true));
            billingTable.addCell(new Cell().add("Due Date ").setFontSize(10f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//            billingTable.addCell(getCell10fLeft("Invoice Due Date",true));
            billingTable.addCell(new Cell().add(invoice.getClientSubscription().getClient().getClientFirstName()+" "+invoice.getClientSubscription().getClient().getClientLastName()).setFontSize(15f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//            billingTable.addCell(getCell10fLeft(invoice.,false));
//            billingTable.addCell(getCell10fLeft("15-02-2023",false));
            Date date = new Date();
            date = invoice.getInvoiceDueDate();
            billingTable.addCell(new Cell().add(String.valueOf(LocalDate.now())).setFontSize(15f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//

            document.add(billingTable);

            Border dashedBorder = new DashedBorder(com.itextpdf.kernel.color.Color.BLUE, 0.5f);
            Table dividerTable1 = new Table(fullWidth);
            dividerTable1.setBorder(dashedBorder);
            // document.add(dividerTable1);
            document.add(space);


            Table threeColTable11 = new Table(sixColWidth);
            threeColTable11.setBackgroundColor(com.itextpdf.kernel.color.Color.BLUE,0.7f);
            threeColTable11.addCell(new Cell().add("DATE").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("ACTIVITY").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("DESCRIPTION").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("QTY").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMarginBottom(15f));
            threeColTable11.addCell(new Cell().add("RATE").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("AMOUNT").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginBottom(15f));


            document.add(threeColTable11);

            Table threeColTable12 = new Table(sixColWidth);
            threeColTable12.addCell(new Cell().add(String.valueOf(LocalDate.now())).setBorder(Border.NO_BORDER));
            threeColTable12.addCell(new Cell().add(invoice.getClientSubscription().getSubscription().getProduct().getProductName()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            threeColTable12.addCell(new Cell().add(invoice.getClientSubscription().getSubscription().getProduct().getProductDescription()).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable12.addCell(new Cell().add("1").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable12.addCell(new Cell().add(String.valueOf(invoice.getInvoiceTotalAmount())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable12.addCell(new Cell().add(String.valueOf(invoice.getInvoiceTotalAmount())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            document.add(threeColTable12);

            document.add(dividerTable1);

            Table threeColTable4 = new Table(sixColWidth);
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("SUB TOTAL").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            threeColTable4.addCell(new Cell().add(String.valueOf(invoice.getInvoiceTotalAmount())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));

            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("VAT").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            double rate = invoice.getInvoiceTotalAmount()*0.18;
            threeColTable4.addCell(new Cell().add(String.valueOf(rate)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));

            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("TOTAL").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            double total = rate + invoice.getInvoiceTotalAmount();
            threeColTable4.addCell(new Cell().add(String.valueOf(total)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));


            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("Paid").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            threeColTable4.addCell(new Cell().add(String.valueOf(invoice.getInvoiceAmountPaid())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("Due Amount").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            threeColTable4.addCell(new Cell().add(String.valueOf(invoice.getInvoiceBalance())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));


            document.add(threeColTable4);
            // document.add(dividerTable1);
            document.add(space);

            Table dividerTable2 = new Table(fullWidth);
            dividerTable2.addCell(new Cell().add("PAYMENT TERMS & CONDITIONS").setBold().setBorder(Border.NO_BORDER).setFontColor(Color.BLACK));
            List<String> termsAndConditions = new ArrayList<>();
            termsAndConditions.add("1. BANK: "+paymentTerms.getBank());
            termsAndConditions.add("2. Account Name: "+paymentTerms.getAccountName());
            termsAndConditions.add("3. Account Number: "+paymentTerms.getAccountNumber());
            termsAndConditions.add("4. Bank Code: "+paymentTerms.getBankCode());
            termsAndConditions.add("5. "+paymentTerms.getDescription());
            termsAndConditions.add("6. Airtel Pay: "+paymentTerms.getAirtelPayCode());
            termsAndConditions.add("7. MoMo Pay: "+paymentTerms.getMtnPayCode());

            for(String s:termsAndConditions){
                dividerTable2.addCell(new Cell().add(s).setBorder(Border.NO_BORDER));
            }

            document.add(dividerTable2);



            document.close();
            System.out.println("Pdf created Successfully");

        } catch(Exception e){

        }
    }


    static Cell getHeaderTextCell(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextValue(String textValue){
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getHeaderTextValue1(String textValue){
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getBillingAndShippingCell(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10fLeft(String textValue, Boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold?myCell.setBold():myCell;
    }

     List<Invoice> getInvoiceByStatus();



    public List<Invoice> getInvoiceByClientSubscriptionId(List<ClientSubscription> clientSubscriptions);

    public List<Invoice> getInvoiceByStatusPaid(Date startDate);


    List<Invoice> getInstances(Search search);
}
