package org.pahappa.systems.core.services.impl;
//imports
import com.googlecode.genericdao.search.Search;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.color.WebColors;
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

import java.util.*;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.clientAccount.ClientAccount;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.companyLogo.CompanyLogo;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.models.payment.PaymentAttachment;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.*;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
//import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.utils.Validate;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.sers.webutils.server.shared.SharedAppData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;

@Getter
@Setter
@Service
@Transactional
public class InvoiceServiceImpl extends GenericServiceImpl<Invoice> implements InvoiceService {
    Date currentDate = new Date();

    Search search = new Search();

    private User loggedInUser;
    private InvoiceTaxService invoiceTaxService;

    private ApplicationEmailService applicationEmailService;

    private List<InvoiceTax> invoiceTaxList;

    private Invoice newInvoice;

    private StreamedContent logoImage;

    private StreamedContent WaterMarkImage;

    private CompanyLogoService companyLogoService;

    private CompanyLogo companyLogo;

    private List<InvoiceTax> invoiceTaxes;

    private ClientAccountService clientAccountService;

    @PostConstruct
    public void init() {
        invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);
        applicationEmailService = ApplicationContextProvider.getBean(ApplicationEmailService.class);
        companyLogoService = ApplicationContextProvider.getBean(CompanyLogoService.class);
        clientAccountService = ApplicationContextProvider.getBean(ClientAccountService.class);
        loggedInUser = SharedAppData.getLoggedInUser();
    }


    @Override
    public Invoice saveInstance(Invoice entityInstance) throws ValidationFailedException, OperationFailedException {

        changeInvoiceNumber(entityInstance);

        if (entityInstance.getInvoiceStatus() == null) {
            entityInstance.setInvoiceStatus(InvoiceStatus.UNPAID);
        }

        // Add a period of 15 days
        int daysToAdd = 5;

        //Calculating due date as start date of the subscription + 5 days
        Calendar cal = Calendar.getInstance();
        cal.setTime(entityInstance.getClientSubscription().getSubscriptionStartDate());
        cal.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Get the updated date
        Date updatedDate = cal.getTime();
        entityInstance.setInvoiceDueDate(updatedDate);

        //calculation of the taxes
        invoiceTaxes = entityInstance.getClientSubscription().getInvoiceTaxList();

        if (invoiceTaxes != null && !invoiceTaxes.isEmpty() ) {
            //Here we are calculating taxes that are not on the invoice total amount but on the product price
            for (InvoiceTax invoiceTax : invoiceTaxes) {
                if (!invoiceTax.getTaxedOnTotalAmount()){
                    entityInstance.setInvoiceTotalAmount((entityInstance.getClientSubscription().getSubscription().getSubscriptionPrice()) + ((invoiceTax.getCurrentTax()/100)*entityInstance.getClientSubscription().getSubscription().getSubscriptionPrice()));
                }
            }

            //Here we are calculating for taxes that are on the invoice total amount with taxes added
            for (InvoiceTax invoiceTax: invoiceTaxes){
                if(invoiceTax.getTaxedOnTotalAmount()){
                    entityInstance.setInvoiceTotalAmount(entityInstance.getInvoiceTotalAmount() + (invoiceTax.getCurrentTax()/100)*entityInstance.getInvoiceTotalAmount());
                }
            }
        } else{
            entityInstance.setInvoiceTotalAmount(entityInstance.getClientSubscription().getSubscription().getSubscriptionPrice());
        }

        //check the persons account incase of available funds we reduct them
        // from what has to be paid

        ClientAccount clientAccount = clientAccountService.getParticularClientAccount( new Search().addFilterEqual("clientId", entityInstance.getClientSubscription().getClient()));

        if(clientAccount.getBalance() > 0) {
            if (clientAccount.getBalance() >= entityInstance.getInvoiceTotalAmount()) {
                entityInstance.setInvoiceAmountPaid(entityInstance.getInvoiceTotalAmount());
                entityInstance.setInvoiceBalance(0.0);
                clientAccount.setBalance(clientAccount.getBalance() - entityInstance.getInvoiceTotalAmount());
            } else {
                entityInstance.setInvoiceAmountPaid(clientAccount.getBalance());
                entityInstance.setInvoiceBalance(entityInstance.getInvoiceTotalAmount() - entityInstance.getInvoiceAmountPaid());
                clientAccount.setBalance(0.0);
            }

            clientAccountService.updateClientAccount(clientAccount);
        }

        Validate.notNull(entityInstance, "Invoice is not saved");

        CustomLogger.log("Calling send invoice");

        saveInvoiceToAppEmail(entityInstance);
        return save(entityInstance);
    }

    public void changeInvoiceNumber(Invoice entityInstance) {
        int instanceCount = countInstances(search.addFilterEqual("recordStatus", RecordStatus.ACTIVE));

        if (instanceCount == 0) {
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d", 1));
        } else {
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d", (instanceCount + 1)));
        }
    }

    @Override
    public void deleteInstance(Invoice instance) throws OperationFailedException {

    }

    @Override
    public boolean isDeletable(Invoice instance) throws OperationFailedException {
        return false;
    }

//    public void changeStatusToPendingApproval(Invoice instance) {
//        instance.setInvoiceStatus(InvoiceStatus.PENDING_APPROVAL);
//        super.save(instance);
//    }

    public void changeStatusToPaid(Invoice instance, double amount) {
        instance.setInvoiceStatus(InvoiceStatus.PAID);
        instance.setInvoiceAmountPaid(instance.getInvoiceAmountPaid()+amount);
        instance.setInvoiceBalance(0.0);
        super.save(instance);

    }

    public void changeStatusToUnpaid(Invoice instance) {
        instance.setInvoiceStatus(InvoiceStatus.UNPAID);
        super.save(instance);
    }

    public void changeStatusToPartiallyPaid(Invoice invoice, double amount) {
        invoice.setInvoiceStatus(InvoiceStatus.PARTIALLY_PAID);
        invoice.setInvoiceAmountPaid(invoice.getInvoiceAmountPaid()+amount);
        invoice.setInvoiceBalance(invoice.getInvoiceTotalAmount() - invoice.getInvoiceAmountPaid());
        super.save(invoice);
        saveInvoiceToAppEmail(invoice);
    }

    @Override
    public List<Invoice> getInvoiceByStatus(Search search) {

        this.search = search;
//        Search search = new Search().setDisjunction(true);

//        search.addFilterEqual("invoiceStatus", InvoiceStatus.UNPAID);
//        search.addFilterEqual("invoiceStatus", InvoiceStatus.PARTIALLY_PAID);

        search.addFilterNotEqual("invoiceStatus", InvoiceStatus.PAID);

//        if(!loggedInUser.hasAdministrativePrivileges()){
//            search.addFilterEqual("createdBy", loggedInUser);
//        }

        return super.search(this.search);
    }

    public void saveInvoiceToAppEmail(Invoice invoice) {

        try {
            CustomLogger.log("Saving to app emails");
            applicationEmailService.saveInvoice(invoice);
            CustomLogger.log("Done saving to app emails");
        } catch (Exception e) {
            CustomLogger.log("Error saving to app emails" + e);
            throw new RuntimeException(e);
        }
    }


    public List<Invoice> getInvoiceByClientSubscriptionId(List<ClientSubscription> clientSubscriptions) {
        List<Invoice> invoiceList = new ArrayList<>();

        for (ClientSubscription clientSubscription : clientSubscriptions) {
            Search search = new Search();
            search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
            System.out.println("Number of client subscriptions  passed " + clientSubscriptions.size());
            search.addFilterEqual("clientSubscription", clientSubscription);
            invoiceList.addAll(super.search(search));
        }

        System.out.println("The invoice List has " + invoiceList.size());

        if (invoiceList == null) {
            System.out.println("Null:" + null);
        } else {
            System.out.println("Not Null:" + invoiceList.size());
        }
        return invoiceList;
    }

    public void saveOrUpdate(Invoice invoice) {
        entityManager.merge(invoice);
    }

    @Override
    public byte[] generateInvoicePdf(Invoice invoice, PaymentTerms paymentTerms,Optional<String> invoiceTitle) {
        try {

            String invoiceTitle1 = invoiceTitle.orElse("Taxed Invoice");

            final String colorCodeGreen = "6DBE46"; // Example blue color code
            final String colorCodeBlue = "2155A3";

            final Color greenColor = WebColors.getRGBColor(colorCodeGreen);
            final Color blueColor = WebColors.getRGBColor(colorCodeBlue);

            // Use a ByteArrayOutputStream to capture the PDF content
            companyLogo = companyLogoService.getAllInstances().get(0);
            buildDownloadableFile(companyLogo);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
            System.out.println("logo image  "+ logoImage);
            System.out.println("waterMark image  "+ WaterMarkImage);

            ImageData logoData = ImageDataFactory.create(IOUtils.toByteArray(logoImage.getStream()));
            Image logo = new Image(logoData);

            ImageData waterMarkData = ImageDataFactory.create(IOUtils.toByteArray(WaterMarkImage.getStream()));
            Image waterMark = new Image(waterMarkData);
            float x = pdfDocument.getDefaultPageSize().getWidth() / 3;
            float y = pdfDocument.getDefaultPageSize().getHeight() / 3;
            waterMark.setFixedPosition(x, 400);
            waterMark.setOpacity(0.1f);
            logo.setFixedPosition(20, 680);
            document.add(logo);
            document.add(waterMark);

            // creating an invoice
            float thirdColumn = 190f;
            float secondColumn = 285f;
            float firstColumn = secondColumn + 150f;
            float[] columnWidth = {firstColumn, secondColumn};
            // float[] oneColumn = {firstColumn};
            float[] fullWidth = {3 * thirdColumn};
            float[] sixColWidth = {thirdColumn, thirdColumn, thirdColumn, thirdColumn, thirdColumn, thirdColumn};
            Paragraph space = new Paragraph("\n");

            Paragraph title = new Paragraph(invoiceTitle1)
                    .setBold()
                    .setFontSize(20f)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginBottom(5f);
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


            Table nestedTable = new Table(new float[]{secondColumn / 2, secondColumn / 2});
            nestedTable.addCell(new Cell().add("Invoice No: ").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            nestedTable.addCell(new Cell().add(invoice.getInvoiceNumber()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            nestedTable.addCell(new Cell().add("Date: ").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            nestedTable.addCell(new Cell().add(((String.valueOf(LocalDate.now())))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

            Border bluBorder = new SolidBorder(blueColor, 2f);
            Table dividerTable = new Table(fullWidth);
            dividerTable.setBorder(bluBorder);


            document.add(table);
            document.add(space);
            document.add(dividerTable);
            // document.add(space);

            Table billingTable = new Table(columnWidth);
            // billingTable.addCell(getCell10fLeft("Client's Name",true));

            billingTable.addCell(new Cell().add("Bill To: ").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//            billingTable.addCell(getCell10fLeft("Bill To: ",true));
            billingTable.addCell(new Cell().add("Due Date ").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//            billingTable.addCell(getCell10fLeft("Invoice Due Date",true));
            billingTable.addCell(new Cell().add(invoice.getClientSubscription().getClient().getClientFirstName() + " " + invoice.getClientSubscription().getClient().getClientLastName()).setFontSize(15f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//            billingTable.addCell(getCell10fLeft(invoice.,false));
//            billingTable.addCell(getCell10fLeft("15-02-2023",false));
            Date date = new Date();
            date = invoice.getInvoiceDueDate();
            billingTable.addCell(new Cell().add(String.valueOf(LocalDate.now())).setFontSize(15f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
//

            document.add(billingTable);


            // Create DashedBorder with the specified color and stroke width

            float strokeWidth = 0.5f; // Example stroke width
            DashedBorder dashedBorder = new DashedBorder(greenColor, strokeWidth);
            Table dividerTable1 = new Table(fullWidth);
            dividerTable1.setBorder(dashedBorder);
            document.add(space);


            Table threeColTable11 = new Table(sixColWidth);
            threeColTable11.setBackgroundColor(blueColor, 0.7f);
            threeColTable11.addCell(new Cell().add("DATE").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("ACTIVITY").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("DESCRIPTION").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("QUANTITY").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMarginBottom(15f));
            threeColTable11.addCell(new Cell().add("RATE").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable11.addCell(new Cell().add("AMOUNT").setBold().setFontColor(com.itextpdf.kernel.color.Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginBottom(15f));


            document.add(threeColTable11);

            Table threeColTable12 = new Table(sixColWidth);
            threeColTable12.addCell(new Cell().add(String.valueOf(LocalDate.now())).setBorder(Border.NO_BORDER));
            threeColTable12.addCell(new Cell().add(invoice.getClientSubscription().getSubscription().getProduct().getProductName()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            threeColTable12.addCell(new Cell().add(invoice.getClientSubscription().getSubscription().getProduct().getProductDescription()).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable12.addCell(new Cell().add("1").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable12.addCell(new Cell().add(String.valueOf(invoice.getClientSubscription().getSubscription().getSubscriptionPrice())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable12.addCell(new Cell().add(String.valueOf(invoice.getClientSubscription().getSubscription().getSubscriptionPrice())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            document.add(threeColTable12);

            document.add(dividerTable1);

            Table threeColTable4 = new Table(sixColWidth);
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("SUB TOTAL").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            threeColTable4.addCell(new Cell().add(String.valueOf(invoice.getClientSubscription().getSubscription().getSubscriptionPrice())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));

            List<InvoiceTax> invoiceTaxList = invoice.getClientSubscription().getInvoiceTaxList();

            Set<InvoiceTax> uniqueInvoiceTaxSet = new HashSet<>(invoice.getClientSubscription().getInvoiceTaxList());

            System.out.println("\n\nThe Size of invoice tax list is: "+ uniqueInvoiceTaxSet.size());
            double rate = 0.0;

            int count = 0;

            for (InvoiceTax invoiceTax: uniqueInvoiceTaxSet){
                count++;
                threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
                threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
                threeColTable4.addCell(new Cell().add(invoiceTax.getTaxName()).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

                if(invoiceTax.getTaxedOnTotalAmount())
                    rate = invoice.getInvoiceTotalAmount() * (invoiceTax.getCurrentTax()/100);
                else
                    rate = (invoice.getClientSubscription().getSubscription().getSubscriptionPrice() + rate) * (invoiceTax.getCurrentTax()/100);
                threeColTable4.addCell(new Cell().add(String.valueOf(rate)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            }

            System.out.println("\n\niterated the invoice tax list "+ count + "times");

            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            threeColTable4.addCell(new Cell().add("").setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
            threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
            threeColTable4.addCell(new Cell().add("OVERALL TOTAL").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            threeColTable4.addCell(new Cell().add(String.valueOf(invoice.getInvoiceTotalAmount())).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));


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
            dividerTable2.addCell(new Cell().add("PAYMENT TERMS & CONDITIONS").setBold().setBorder(Border.NO_BORDER).setFontColor(blueColor));
            List<String> termsAndConditions = new ArrayList<>();
            termsAndConditions.add("1. BANK: " + paymentTerms.getBank());
            termsAndConditions.add("2. Account Name: " + paymentTerms.getAccountName());
            termsAndConditions.add("3. Account Number: " + paymentTerms.getAccountNumber());
            termsAndConditions.add("4. Bank Code: " + paymentTerms.getBankCode());
            termsAndConditions.add("5. " + paymentTerms.getDescription());
            termsAndConditions.add("6. Airtel Pay: " + paymentTerms.getAirtelPayCode());
            termsAndConditions.add("7. MoMo Pay: " + paymentTerms.getMtnPayCode());

            for (String s : termsAndConditions) {
                dividerTable2.addCell(new Cell().add(s).setBorder(Border.NO_BORDER));
            }

            document.add(dividerTable2);


            document.close();

            pdfDocument.close();

            // Convert the ByteArrayOutputStream to a byte array
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            // Set the generated PDF bytes to the invoice
            invoice.setInvoicePdf(pdfBytes);

            // Save the invoice to the database
            saveOrUpdate(invoice);
            System.out.println("Pdf created Successfully");
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            System.out.println("Error Occurred: " + e.getMessage());
            return null;
        }
    }

    public StreamedContent buildDownloadableFile(CompanyLogo companyLogo){
        InputStream inputStream = new ByteArrayInputStream(companyLogo.getLogoName());
        logoImage = new DefaultStreamedContent(inputStream, companyLogo.getLogoPath());

        InputStream inputStream1 = new ByteArrayInputStream(companyLogo.getWaterMarkName());
        WaterMarkImage = new DefaultStreamedContent(inputStream1, companyLogo.getWaterMarkPath());
        return logoImage;
    }
}