package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.sendSalesAgentReminder.SendSalesAgentReminder;
import org.pahappa.systems.core.services.*;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.activation.*;

@Service
@Transactional
public class ApplicationEmailServiceImpl extends GenericServiceImpl<AppEmail> implements ApplicationEmailService {

    static boolean locked= false;
    private Invoice invoiceObject;

    private Payment paymentObject;

    private ClientSubscriptionService clientSubscriptionService;

    public void setClientInvoices(List<Invoice> clientInvoices) {
        this.clientInvoices = clientInvoices;
    }

    @Getter
    private List<Invoice> clientInvoices;

    private InvoiceService invoiceService;


    private Invoice invoice;
    private PaymentTermsService paymentTermsService;

    private EmailSetupService emailSetupService;

    private EmailSetup emailSetup;

    private List<ClientSubscription> clientSubscriptionsList;


    @PostConstruct
    public void init(){
        paymentTermsService = ApplicationContextProvider.getBean(PaymentTermsService.class);
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        emailSetup = emailSetupService.getActiveEmail();
    }

    public ApplicationEmailServiceImpl(){
        paymentTermsService = ApplicationContextProvider.getBean(PaymentTermsService.class);
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        emailSetup = emailSetupService.getActiveEmail();
    }

    @Override
    public AppEmail saveInstance(AppEmail entityInstance) throws ValidationFailedException, OperationFailedException {

        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(AppEmail instance) throws OperationFailedException {
        return false;
    }

    public void saveInvoice(Invoice invoiceObject, String emailSubject){
        EmailSetup(invoiceObject, emailSubject);

    }

    public void saveBalanceInvoice(Invoice invoiceObject, String emailSubject){
        EmailSetup(invoiceObject, emailSubject);

    }

    public void saveReciept(Payment paymentObject, String emailSubject){
        EmailSetup(paymentObject,emailSubject);

    }

    private void EmailSetup(Object object, String emailSubject) {
        AppEmail appEmail = new AppEmail();
        String recipientEmail;

        if(Invoice.class.isInstance(object)){
            this.invoiceObject = (Invoice) object;
            appEmail.setInvoiceObject(invoiceObject);
            recipientEmail = invoiceObject.getClientSubscription().getClient().getClientEmail();
            System.out.println("Invoice client email is: "+invoiceObject.getClientSubscription().getClient().getClientEmail());

        }else{
            this.paymentObject= (Payment) object;
            appEmail.setPaymentObject(this.paymentObject);
            recipientEmail = paymentObject.getInvoice().getClientSubscription().getClient().getClientEmail();

        }

        System.out.println(emailSetup.getSenderEmail());
        appEmail.setSenderEmail(emailSetup.getSenderEmail());

        appEmail.setSenderPassword(emailSetup.getSenderPassword());

        appEmail.setReceiverEmail(recipientEmail);

        appEmail.setEmailSubject(emailSubject);

        appEmail.setEmailMessage("");

        appEmail.setEmailStatus(false);

        try {
            saveInstance(appEmail);
        } catch (ValidationFailedException e) {
            throw new RuntimeException(e);
        } catch (OperationFailedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSavedInvoices(){
        if(!locked){
            locked =true;
            System.out.println("\n\n\nStarting send emails\n\n\n");
            Search search = new Search();
            search.addFilterEqual("emailStatus", false);
            List<AppEmail> appEmails = super.search(search);
            for(AppEmail appEmail: appEmails){
                try {

                    if(appEmail.getInvoiceObject() == null){
                        //Here we send the recepit us
                        sendEmail(appEmail.getReceiverEmail(), appEmail.getEmailSubject(), appEmail.getEmailMessage(), appEmail.getPaymentObject());
                    }else {
                        sendEmail(appEmail.getReceiverEmail(), appEmail.getEmailSubject(), appEmail.getEmailMessage(), appEmail.getInvoiceObject());
                    }
                    appEmail.setEmailStatus(true);
                    super.update(appEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("\n\n\nCompleting send emails\n\n\n");
            locked = false;
        }

    }

    byte[] pdfBytes;
    byte[] pdfContent;
    File pdfFile;
    public void sendEmail(String recipientEmail, String subject, String messageToSend, Object object) throws IOException {
        String filePath;
//        byte[] pdfBytes;

        if (Invoice.class.isInstance(object)){
            System.out.println("Smtp Host is: "+emailSetup.getSmtpHost());
            System.out.println(object.getClass().getName());
            System.out.println("The Account Name is: "+paymentTermsService.getAllInstances().stream().findFirst().orElse(new PaymentTerms()).getAccountName());

            pdfBytes = invoiceService.generateInvoicePdf((Invoice) object,paymentTermsService.getAllInstances().stream().findFirst().orElse(new PaymentTerms()));
//            filePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/automated-invoicing/Invoice.pdf";

            pdfContent = ((Invoice) object).getInvoicePdf();

            // Save the PDF content to a file
            pdfFile = savePdfToFile(pdfContent);

            System.out.println("we are done generating");
        }else{
            PaymentService.generateReceipt((Payment) object);
            filePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/automated-invoicing/Invoice.pdf";
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", emailSetup.getSmtpHost());
        props.put("mail.smtp.port", emailSetup.getSmtpPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Set up the session with the authentication details
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSetup.getSenderEmail(), emailSetup.getSenderPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSetup.getSenderEmail(), emailSetup.getSenderUsername()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Dear Client,\n\nPlease find the attached invoice.\n\nBest Regards,\n"+ emailSetup.getSenderUsername());

            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(pdfFile);
            pdfBodyPart.setDataHandler(new DataHandler(source));
            pdfBodyPart.setFileName("Invoice.pdf");

            // Create Multipart and add both body parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textBodyPart);
            multipart.addBodyPart(pdfBodyPart);

            // Set the Multipart as the message content
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Invoice sent successfully.");

            SendSalesAgentReminder reminder = new SendSalesAgentReminder();

            if(object instanceof Invoice) {
                reminder.sendSalesAgentReminder((Invoice) object);
            }
            else{
                System.out.println("Not an instance of invoice");
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            System.out.println("Issue Occurred :"+ex.getMessage());
        } finally {
            // Delete the temporary file if it was created
            if (pdfFile != null && pdfFile.exists()) {
                pdfFile.delete();
            }
        }
    }

    private File savePdfToFile(byte[] pdfContent) throws IOException, IOException {
        // Create a temporary file to save the PDF content
        File pdfFile = File.createTempFile("invoice", ".pdf");

        // Write the PDF content to the file
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            fos.write(pdfContent);
        }

        return pdfFile;
    }

    public void sendClientReminder(){
        if(!locked){
            locked=true;
//            invoiceService =ApplicationContextProvider.getBean(InvoiceService.class);
//            clientInvoices = invoiceService.getInvoiceByStatus();
//            // getInvoiceByStatus returns all invoices that are unpaid and partially paid
//            System.out.println(clientInvoices.size());
//
//            if(clientInvoices.isEmpty()){
//                System.out.println("No unpaid client invoices");
//            }else{
//                for(Invoice clientInvoice: clientInvoices) {
//                    System.out.println("Invoice Client reminder:" + clientInvoice.getClientSubscription().getClient().getClientEmail());
//
//                    //check if there is reminder with the same invoice number and has a status not sent in the appEmail table
//
//                    Search search = new Search();
//                    search.addFilterEqual("invoiceObject.invoiceNumber", clientInvoice.getInvoiceNumber());
//                    search.addFilterEqual("emailStatus", false);
//                    List<AppEmail> appEmails = super.search(search);
//
//                    if (appEmails.isEmpty()) {
//                        if (clientInvoice.getInvoiceStatus() == InvoiceStatus.UNPAID){
//                            System.out.println("No reminder with the same invoice number");
//                            //Check if the current date is 10, 5, 2 days from the invoice due date
//                            Date date = new Date();
//                            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                            int currentDay = localDate.getDayOfMonth();
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTime(clientInvoice.getInvoiceDueDate());
//                            int dueDate = cal.get(Calendar.DAY_OF_MONTH);
//                            int difference = dueDate - currentDay;
//                            System.out.println("Difference from the due date from the invoice:" + difference);
//                            if (difference == 10 || difference == 5 || difference == 2) {
//                                System.out.println("Difference is 10, 5 or 2");
//                                saveInvoice(clientInvoice, "Invoice Payment Reminder for Invoice "+ clientInvoice.getInvoiceNumber());
//                            }
//                        }
//
//                    }
//                    else {
//                        System.out.println("Reminder with the same invoice number hasn't yet been sent");
//                    }
//
//                }
//
//            }

            clientSubscriptionsList = clientSubscriptionService.getAllInstances();
            if(clientSubscriptionsList.isEmpty()){
                System.out.println("No client subscriptions");
            }else{
                for(ClientSubscription clientSubscription: clientSubscriptionsList){
                    Date currentDate = new Date();
                    LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int currentDay = localDate.getDayOfMonth();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(clientSubscription.getSubscriptionEndDate());
                    int dueDate = cal.get(Calendar.DAY_OF_MONTH);
                    int difference = dueDate - currentDay;
                    int differenceAfter = currentDay - dueDate;
                    System.out.println("Difference from the due date from the invoice:" + difference);
                    if (difference == clientSubscription.getSubscription().getNumberOfDaysBefore() || difference == clientSubscription.getSubscription().getNumberOfWeeksBefore() || difference == clientSubscription.getSubscription().getNumberOfMonthsBefore() ) {
                        //send the reminder
                    }
                    else if(differenceAfter == clientSubscription.getSubscription().getNumberOfDaysAfter() || differenceAfter == clientSubscription.getSubscription().getNumberOfWeeksAfter() || differenceAfter == clientSubscription.getSubscription().getNumberOfMonthsAfter()){
                        //send the reminder
                    }
                    else{
                        System.out.println("No reminder to be sent");
                    }
                }
            }


            locked=false;
        }
    }



    public void generateInvoiceForNewClientSubscription() {
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance(); //create a calendar instance and set it to the current date
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH,1);

        clientSubscriptionsList = clientSubscriptionService.getClientSubscriptionsByEndDate(currentDate);

        for (ClientSubscription clientSubscription: clientSubscriptionsList) {
            clientSubscription.setSubscriptionStartDate(calendar.getTime());
            clientSubscription.setSubscriptionEndDate(calculateEndDate(clientSubscription.getSubscriptionStartDate(),clientSubscription.getSubscription().getSubscriptionTimeUnits().toString()));
            clientSubscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
            try {
                clientSubscriptionService.saveInstance(clientSubscription);
            } catch (ValidationFailedException e) {
                throw new RuntimeException(e);
            } catch (OperationFailedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Date calculateEndDate(Date startDate, String selectedTimeUnit) {
        if (startDate != null && selectedTimeUnit != null && !selectedTimeUnit.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            System.out.println("Selected time unit is "+ selectedTimeUnit);

            switch (selectedTimeUnit) {
                case "YEARLY":
                    calendar.add(Calendar.YEAR, 1);
                    break;
                case "QUARTERLY":
                    calendar.add(Calendar.MONTH, 3);
                    break;
                case "MONTHLY":
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case "WEEKLY":
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                // Add more cases for other time units if needed
                default:
                    // Handle unexpected time units
                    break;
            }

            // Adjust to the last day of the month
            return calendar.getTime();
        }

        return null; // or throw an exception if needed
    }
}
