package org.pahappa.systems.core.services;

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
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.services.base.GenericService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface InvoiceService extends GenericService<Invoice> {
    void changeStatusToPendingApproval(Invoice invoice);
    void changeStatusToPaid(Invoice invoice, double amount);

    void changeStatusToPartiallyPaid(Invoice invoice, double amount);

    static void generateInvoicePdf(Invoice invoice){
        try{
            String path = "/home/devclinton/Documents/Pahappa/automated-invoicing/Receipt.pdf";
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
            String imagePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/Pahappa logo1.jpeg";
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            float x = pdfDocument.getDefaultPageSize().getWidth()/3;
            float y = pdfDocument.getDefaultPageSize().getHeight()/2;
            // image.setFixedPosition(x, y/3);
            image.setFixedPosition(80, 670);
//            image.setFixedPosition(x, y/3);
            // image.setOpacity(0.1f);
            document.add(image);
            // creating an invoice
            float thirdColumn = 190f;
            float secondColumn = 285f;
            float firstColumn = secondColumn+150f;
            float[] columnWidth = {firstColumn,secondColumn};
            // float[] oneColumn = {firstColumn};
            float[] fullWidth = {3*thirdColumn};
            float[] threeColWidth = {thirdColumn,thirdColumn,thirdColumn};
            float[] fourColWidth = {thirdColumn,thirdColumn,thirdColumn,thirdColumn};
            Paragraph space = new Paragraph("\n");

            // creating the table
            Table table = new Table(columnWidth);
            table.addCell(new Cell().add("Invoice").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
            table.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add("Pahappa Company Limited").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add("Email:Info@pahappa.com").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add("P.O BOX 18782 Ntinda Muteesa II ROAD Kampala Uganda").setBorder(Border.NO_BORDER));
            Table nestedTable = new Table(new float[]{secondColumn/2,secondColumn/2});
            nestedTable.addCell(getHeaderTextCell("Invoice No: "));
            nestedTable.addCell(getHeaderTextValue(invoice.getInvoiceNumber()));
            nestedTable.addCell(getHeaderTextCell("Invoice Due Date: "));
            nestedTable.addCell(getHeaderTextValue(invoice.getInvoiceDueDate().toString()));
            table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

            Border bluBorder = new SolidBorder(com.itextpdf.kernel.color.Color.BLUE, 2f);
            Table dividerTable = new Table(fullWidth);
            dividerTable.setBorder(bluBorder);


            document.add(table);
            document.add(space);
            document.add(dividerTable);

            Table billingTable = new Table(columnWidth);
            billingTable.addCell(getCell10fLeft("Client's Name",true));
            billingTable.addCell(getCell10fLeft("Client's Contact",true));
            billingTable.addCell(getCell10fLeft(invoice.getClientSubscription().getClient().getClientFirstName()+" "+invoice.getClientSubscription().getClient().getClientLastName(),false));
            billingTable.addCell(getCell10fLeft(invoice.getClientSubscription().getClient().getClientContact(),false));

            billingTable.addCell(getCell10fLeft("Client's Email",true));
            billingTable.addCell(getCell10fLeft("",true));
            billingTable.addCell(getCell10fLeft(invoice.getClientSubscription().getClient().getClientEmail(),false));
            billingTable.addCell(getCell10fLeft("",false));
            document.add(billingTable);

            Border dashedBorder = new DashedBorder(com.itextpdf.kernel.color.Color.BLUE, 0.5f);
            Table dividerTable1 = new Table(fullWidth);
            dividerTable1.setBorder(dashedBorder);
            document.add(dividerTable1);
            document.add(space);

            Paragraph prodParagraph = new Paragraph("Service Details");
            document.add(prodParagraph.setBold());

            Table threeColTable1 = new Table(threeColWidth);
            threeColTable1.setBackgroundColor(com.itextpdf.kernel.color.Color.BLUE,0.7f);
            threeColTable1.addCell(new Cell().add("Description").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setBorder(Border.NO_BORDER));
            threeColTable1.addCell(new Cell().add("Quantity").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable1.addCell(new Cell().add("Price").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginBottom(15f));
//            List<Product> productList = new ArrayList<>();
//            productList.add(new Product("Website",2,150000.0));
//            productList.add(new Product("EgoSMS",6,80000.0));
//            productList.add(new Product("System Dev't",1,750000));
            document.add(threeColTable1);

            double totalSum = 0f;
            Table threeColTable2 = new Table(threeColWidth);
//            for(Product p:productList){
//                double total= p.getQuantity()*p.getPrice();
//                totalSum += total;
//                threeColTable2.addCell(new Cell().add(p.getDescription()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//                threeColTable2.addCell(new Cell().add(String.valueOf(p.getQuantity())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
//                threeColTable2.addCell(new Cell().add(String.valueOf(total)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
//            }
            document.add(threeColTable2);

            float oneTwo[] = {thirdColumn+125f,thirdColumn*2};
            Table threeColTable3 = new Table(oneTwo);
            threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable3.addCell(new Cell().add(dividerTable1).setBorder(Border.NO_BORDER));
            document.add(threeColTable3);

            Table threeColTable4 = new Table(threeColWidth);

            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("Total").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add(String.valueOf(totalSum)).setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            document.add(threeColTable4);
            document.add(dividerTable1);
            document.add(space);
            document.add(dividerTable.setBorder(new SolidBorder(Color.BLUE,1)).setMarginBottom(15f));


            //still working on this
            Paragraph payParagraph = new Paragraph("Payment Information");
            document.add(payParagraph.setBold());
            Table threeColTable11 = new Table(fourColWidth);
            threeColTable11.setBackgroundColor(com.itextpdf.kernel.color.Color.BLUE,0.7f);
            threeColTable11.addCell(new Cell().add("Sub-Total").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("Taxes").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("Discounts").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("Total Amount Due").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginBottom(15f));
//            List<Product> clientPayments = new ArrayList<>();
            document.add(threeColTable11);
            document.add(space);

            Table dividerTable2 = new Table(fullWidth);
            dividerTable2.addCell(new Cell().add("PAYMENT TERMS").setBold().setBorder(Border.NO_BORDER).setFontColor(Color.BLUE));
            List<String> termsAndConditions = new ArrayList<>();
            termsAndConditions.add("1. BANK: CENTENARY BANK");
            termsAndConditions.add("2. Account Name: PAHAPPA LIMITED");
            termsAndConditions.add("3. Account Number: 3100062448");
            termsAndConditions.add("4. Bank Code: CERBUGKA");
            termsAndConditions.add("5. Make all cheques payable to Pahappa Limited");
            termsAndConditions.add("6. Airtel Pay: 1190866");
            termsAndConditions.add("7. MoMo Pay: 608913");

            for(String s:termsAndConditions){
                dividerTable2.addCell(new Cell().add(s).setBorder(Border.NO_BORDER));
            }

            document.add(dividerTable2);
            document.close();
//            System.out.println("Pdf created Successfully");

        } catch(Exception e){
            System.out.println("Image was not found");

        }
    }

    static Cell getHeaderTextCell(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextValue(String textValue){
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



    public Invoice getInvoiceByClientSubscriptionId(String id);

    public List<Invoice> getInvoiceByStatusPaid(Date startDate);


}
