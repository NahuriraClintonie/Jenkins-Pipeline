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
    public String INVOICE_TEMPLATE =  "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Invoice</title>\n" +
            "    <style>\n" +
            "        .about {\n" +
            "            border-style: hidden;\n" +
            "            font-family: 'Times New Roman', Times, serif;\n" +
            "            color:  blue;\n" +
            "        }\n" +
            "        .about tr {\n" +
            "            border-style: hidden;\n" +
            "        }\n" +
            "\n" +
            "        table, th, td {\n" +
            "            width: 900px;\n" +
            "            border: 1px solid black;\n" +
            "               \n" +
            "        }\n" +
            "        .owner {\n" +
            "            border-style:hidden;\n" +
            "        }\n" +
            "\n" +
            "        table {\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "        \n" +
            "        .invoice-details tr:nth-child(odd) {background-color: grey;} \n" +
            "            \n" +
            "        .invoice-faqs {\n" +
            "            font-size: small;\n" +
            "        }\n" +
            "        .invoice-faqs tr {\n" +
            "            border-style:hidden;\n" +
            "\n" +
            "        }\n" +
            "        .invoice-sum {\n" +
            "            color: blue;\n" +
           "            width: 400px;\n" +
            "           right:400px"+
            "        }\n" +
            "\n" +
            "       .info{\n"+
            "           width:900px;\n"+
            "        }\n" +
            "\n" +
            "    </style>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "        <table class=\"about\">\n" +
            "            <tr><td>Muteesa 2 Road</td></tr>\n" +
            "            <tr><td>Ntinda</td></tr>\n" +
            "            <tr><td>Kampala</td></tr>\n" +
            "            <tr><td><br/></td></tr>\n" +
            "            <tr><td>Tel- </td></tr>\n" +
            "            <tr><td>TIN- </td></tr>\n" +
            "        </table>\n" +
            "        <br/>\n" +
            "        <div >\n" +
            "        <table class=\"owner\">\n" +
            "            <tr>\n" +
            "                <thead style=\"background-color:#4169e1;color:white;\">\n" +
            "                <th style=\"text-align: left;\">BILL TO</th>\n" +
            "                <th>Date</th>\n" +
            "                <th>Invoice No.</th>\n" +
            "            </thead>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td>%s</td>\n" +
            "                <td>%s</td>\n" +
            "                <td>%s</td>\n" +
            "            </tr>\n" +
            "        </table>\n" +
            "        <br/>\n" +
            "        <table class=\"owner\">\n" +
            "            <tr>\n" +
            "                <thead style=\"background-color:#4169e1; color:white\">\n" +
            "                <th style=\"text-align: left;\">P.O No. </th>\n" +
            "                <th>Terms</th>\n" +
            "                <th>Due Date</td>\n" +
            "                <th style=\"width:fit-content\">Account No.</th>\n" +
            "                <th>Project</th>\n" +
            "            </thead>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td></td>\n" +
            "                <td></td>\n" +
            "                <td>%s</td>\n" +
            "                <td></td>\n" +
            "                <td></td>\n" +
            "            </tr>\n" +
            "        </table>\n" +
            "    </div>\n" +
            "        <br/>\n" +
            "        <div>\n" +
            "        <table class=\"invoice-details\">\n" +
            "            <tr>\n" +
            "                <th style=\"text-align: left;\">Description</th>\n" +
            "                <th>Quantity</th>\n" +
            "                <th>Rate</th>\n" +
            "                <th>Amount</th>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>%s</td>\n" +
            "                <td>1</td>\n" +
            "                <td>%d</td>\n" +
            "                <td>%f</td>\n" +
            "            </tr>\n" +
            "        \n" +
            "        </table>\n" +
            "    </div>\n" +
            "        <br/>\n" +
            "\n" +
            "        <div class=\"info\"  style=\"display:flex\">\n" +
            "        <table class=\"invoice-faqs\">\n" +
            "            <tr><td>Terms & Conditions</td></tr>\n" +
            "            <tr><td>Currency: UGX</td></tr>\n" +
            "            <tr><td>Bank: Centenary Bank</td></tr>\n" +
            "            <tr><td>Account Name: PAHAPPA LIMITED</td></tr>\n" +
            "            <tr><td>Account No. 3100062448</td></tr>\n" +
            "            <tr><td>Bank Code CERBUGKA</td></tr>\n" +
            "            <tr><td>Make ALL Cheques payable to PAHAPPA LIMITED</td></tr>\n" +
            "            <tr><td>Airtel Pay 1190866</td></tr>\n" +
            "            <tr><td>Momo Pay 608913</td></tr>\n" +
            "        </table>\n" +
            "        <br/>\n" +
            "        <table class=\"invoice-sum\">\n" +
            "            <tr>\n" +
            "                <th>Total</th>\n" +
            "                <td>%f</td>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <th>Payments/ Credits</th>\n" +
            "                <td>0</td>\n" +
            "            </tr>   \n" +
            "            <tr>\n" +
            "                <th>Balance Due</th>\n" +
            "                <td>%f</td>\n" +
            "            </tr>   \n" +
            "        </table> \n" +
            "    </div>  \n" +
            "</body>\n" +
            "</html>" ;

     static String generateInvoice(Invoice invoice){

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         String invoiceDate = dateFormat.format(invoice.getDateCreated());
         String dueDate = dateFormat.format(invoice.getInvoiceDueDate());
         String firstName = invoice.getClientSubscription().getClient().getClientFirstName();
         String productName = invoice.getClientSubscription().getSubscription().getProduct().getProductName();
        int duration = invoice.getClientSubscription().getSubscription().getSubscriptionDuration();
        double price = invoice.getClientSubscription().getSubscriptionPrice();

         return String.format(INVOICE_TEMPLATE,
                 firstName,
            invoiceDate,
            invoice.getInvoiceNumber(),
            dueDate,
            //invoice.getClientSubscription().getSubscription().getProduct().getProductName(),
                 productName,
            //invoice.getClientSubscription().getSubscription().getSubscriptionDuration(),
                 duration,
            //invoice.getClientSubscription().getSubscription_price(),
                 price,
            invoice.getInvoiceTotalAmount(),
            invoice.getInvoiceBalance()
         );
    }

    static void generateInvoicePdf(Invoice invoice){
        try{
            String path = "E:\\Pahappa Documents\\automated-invoicing\\Invoice.pdf";
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
//            System.out.println("Document created successfuly");
            String imagePath = "E:\\Pahappa Documents\\automated-invoicing\\pahappaLogo2.jpg";
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            float x = pdfDocument.getDefaultPageSize().getWidth()/3;
            float y = pdfDocument.getDefaultPageSize().getHeight()/2;
            // image.setFixedPosition(x, y/3);
            image.setFixedPosition(x, y/3);
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
