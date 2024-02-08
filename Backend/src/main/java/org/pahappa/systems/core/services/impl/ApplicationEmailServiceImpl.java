package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.constants.SubscriptionStatus;

import org.pahappa.systems.core.constants.TemplateType;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.emailTemplate.EmailTemplate;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.activation.*;

import java.util.regex.Pattern;

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

    Map<String, String> placeholders = new HashMap<>();

    private Invoice invoice;
    private PaymentTermsService paymentTermsService;

    private EmailSetupService emailSetupService;

    private EmailSetup emailSetup;

    private List<ClientSubscription> clientSubscriptionsList;

    private EmailTemplateService emailTemplateService;
    private EmailTemplate emailTemplate;
    private String emailSubject;
    private String emailMessage;
    private String updatedEmailMessage;

    private String recipientEmail;


    @PostConstruct
    public void init(){
        paymentTermsService = ApplicationContextProvider.getBean(PaymentTermsService.class);
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);

        emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);
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

    public void saveInvoice(Invoice invoiceObject){
        AppEmail appEmail = new AppEmail();
        if(Invoice.class.isInstance(invoiceObject)){
            this.invoiceObject = invoiceObject;
            appEmail.setInvoiceObject(invoiceObject);
            recipientEmail = invoiceObject.getClientSubscription().getClient().getClientEmail();

            if(invoiceObject.getInvoiceTotalAmount() > invoiceObject.getInvoiceAmountPaid()){
                if(invoiceObject.getInvoiceAmountPaid() == 0) {
                    emailSubject = getEmailTemplateSubject(TemplateType.NEW_SUBSCRIPTION);
                    emailMessage = getEmailTemplateMessage(TemplateType.NEW_SUBSCRIPTION);
                }else {
                    emailSubject = getEmailTemplateSubject(TemplateType.PARTIAL_PAYMENT);
                    emailMessage = getEmailTemplateMessage(TemplateType.PARTIAL_PAYMENT);
                }
            } else if(invoiceObject.getInvoiceTotalAmount() == invoiceObject.getInvoiceAmountPaid()) {
                emailSubject = getEmailTemplateSubject(TemplateType.FULL_PAYMENT);
                emailMessage = getEmailTemplateMessage(TemplateType.FULL_PAYMENT);
            }

            placeholders.put("fullName", invoiceObject.getClientSubscription().getClient().getClientFirstName()+" "+invoiceObject.getClientSubscription().getClient().getClientLastName());
            placeholders.put("SubscriptionName", invoiceObject.getClientSubscription().getSubscription().getSubscriptionName()); // Replace with actual data
            placeholders.put("SubscriptionExpiryDate", invoiceObject.getClientSubscription().getSubscriptionEndDate().toString()); // Replace with actual data

            System.out.println("Invoice client email is: "+invoiceObject.getClientSubscription().getClient().getClientEmail());

        }
        EmailSetup(invoiceObject, emailMessage, emailSubject, recipientEmail);
    }

    public void saveReciept(Payment paymentObject, String emailSubject){

    }

    private void EmailSetup(Invoice invoiceObject, String emailMessage, String emailSubject, String recipientEmail) {
        AppEmail appEmail = new AppEmail();

        if(invoiceObject != null){
            appEmail.setInvoiceObject(invoiceObject);
        }

        emailSetup = emailSetupService.getActiveEmail();

        // Replace placeholders in emailSubject and emailMessage
        updatedEmailMessage = replacePlaceholders(emailMessage, placeholders);

        appEmail.setEmailSubject(emailSubject);
        System.out.println(emailSetup.getSenderEmail());
        appEmail.setSenderEmail(emailSetup.getSenderEmail());

        appEmail.setSenderPassword(emailSetup.getSenderPassword());

        appEmail.setReceiverEmail(recipientEmail);

        appEmail.setEmailMessage(updatedEmailMessage);

        appEmail.setEmailStatus(false);

        try {
            saveInstance(appEmail);
        } catch (ValidationFailedException e) {
            throw new RuntimeException(e);
        } catch (OperationFailedException e) {
            throw new RuntimeException(e);
        }
    }

    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = entry.getValue();
            template = template.replace(placeholder, value);
        }

        return template;
    }

    private String getEmailTemplateSubject(TemplateType templateType) {

        emailTemplate = emailTemplateService.getEmailTemplateByType(templateType);

        return emailTemplate != null ? emailTemplate.getSubject() : "An Invoice from Pahappa Limited";
    }

    private String getEmailTemplateMessage(TemplateType templateType) {
        emailTemplate = emailTemplateService.getEmailTemplateByType(templateType);

        if (emailTemplate != null) {
            return removeHtmlTags(emailTemplate.getTemplate());
        } else {
            return "An Invoice from Pahappa Limited";
        }
    }

    private String removeHtmlTags(String html) {
        // Remove HTML tags using regular expression
        return html.replaceAll("<[^>]*>", "");
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
        emailSetup = emailSetupService.getActiveEmail();
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
            System.out.println("Not an instance of invoice");
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

        if(Invoice.class.isInstance(object)){
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailSetup.getSenderEmail(), emailSetup.getSenderUsername()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);

                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setText(updatedEmailMessage+",\n"+ emailSetup.getSenderUsername());
                message.setText("Dear Client,\n\nPlease find the attached invoice.\n\nBest Regards,\n"+ emailSetup.getSenderUsername());

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
        else {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailSetup.getSenderEmail(), emailSetup.getSenderUsername()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);

                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setText(messageToSend+",\n"+ emailSetup.getSenderUsername());

                // Create Multipart and add both body parts
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textBodyPart);

                // Set the Multipart as the message content
                message.setContent(multipart);

                Transport.send(message);

                System.out.println("Reminder sent successfully.");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (Exception ex) {
                System.out.println("Issue Occurred :"+ex.getMessage());
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
            clientSubscriptionsList = clientSubscriptionService.getAllInstances();
            if(clientSubscriptionsList.isEmpty()){
                System.out.println("No client subscriptions");
            }else{
                for(ClientSubscription clientSubscription: clientSubscriptionsList){
                    Date currentDate = new Date();
                    LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int currentDay = localDate.getDayOfMonth();
                    int currentMonth = localDate.getMonthValue();
                    int currentYear = localDate.getYear();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(clientSubscription.getSubscriptionEndDate());
                    int dueDay = cal.get(Calendar.DAY_OF_MONTH);
                    int dueMonth = cal.get(Calendar.MONTH) + 1; // Adding 1 because Calendar months are zero-based
                    int dueYear = cal.get(Calendar.YEAR);

                    // Calculating difference before the due date
                    int differenceBeforeInDays = dueDay - currentDay;
                    long differenceBeforeInWeeks = ChronoUnit.WEEKS.between(localDate.withDayOfMonth(1), LocalDate.of(dueYear, dueMonth, dueDay));
                    long differenceBeforeInMonths = ChronoUnit.MONTHS.between(localDate.withDayOfMonth(1), LocalDate.of(dueYear, dueMonth, dueDay));

                    System.out.println("\n**********************************************\n");
                    System.out.println("Difference before in days: "+differenceBeforeInDays);
                    System.out.println("Difference before in weeks: "+differenceBeforeInWeeks);
                    System.out.println("Difference before in months: "+differenceBeforeInMonths);
                    System.out.println("\n**********************************************\n");


                    // Calculating difference after the due date
                    int differenceAfterInDays = currentDay - dueDay;
                    long differenceAfterInWeeks = ChronoUnit.WEEKS.between(LocalDate.of(dueYear, dueMonth, dueDay), localDate.withDayOfMonth(localDate.lengthOfMonth()));
                    long differenceAfterInMonths = ChronoUnit.MONTHS.between(LocalDate.of(dueYear, dueMonth, dueDay), localDate.withDayOfMonth(localDate.lengthOfMonth()));

                    System.out.println("\n**********************************************\n");
                    System.out.println("Difference after in days: "+differenceAfterInDays);
                    System.out.println("Difference after in weeks: "+differenceAfterInWeeks);
                    System.out.println("Difference after in months: "+differenceAfterInMonths);
                    System.out.println("\n**********************************************\n");

                    recipientEmail = clientSubscription.getClient().getClientEmail();

                    if (differenceBeforeInDays == clientSubscription.getSubscription().getNumberOfDaysBefore() || differenceBeforeInWeeks == clientSubscription.getSubscription().getNumberOfWeeksBefore() || differenceBeforeInMonths == clientSubscription.getSubscription().getNumberOfMonthsBefore() ) {

                        if(differenceBeforeInDays == clientSubscription.getSubscription().getNumberOfDaysBefore()){
                            placeholders.put("daysBeforeExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfDaysBefore()));
                            placeholders.put("period", "days");
                        }else if(differenceBeforeInWeeks == clientSubscription.getSubscription().getNumberOfWeeksBefore()){
                            placeholders.put("weeksBeforeExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfWeeksBefore()));
                            placeholders.put("period", "weeks");
                        }else if(differenceBeforeInMonths == clientSubscription.getSubscription().getNumberOfMonthsBefore()){
                            placeholders.put("{monthsBeforeExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfMonthsBefore()));
                            System.out.println("Months before expiry: "+clientSubscription.getSubscription().getNumberOfMonthsBefore());
                            placeholders.put("period", "months");
                        }else {
                            System.out.println("No reminder to be sent");
                        }
                        emailSubject = getEmailTemplateSubject(TemplateType.REMINDER_BEFORE_EXPIRY);
                        emailMessage = getEmailTemplateMessage(TemplateType.REMINDER_BEFORE_EXPIRY);
                        placeholders.put("fullName", clientSubscription.getClient().getClientFirstName() +" "+ clientSubscription.getClient().getClientLastName());
                        placeholders.put("SubscriptionName", clientSubscription.getSubscription().getSubscriptionName()); // Replace with actual data
                        placeholders.put("SubscriptionExpiryDate", clientSubscription.getSubscriptionEndDate().toString()); // Replace with actual data
                        EmailSetup(null, replacePlaceholders(emailMessage,placeholders), emailSubject, recipientEmail);

                    }
                    else if(differenceAfterInDays == clientSubscription.getSubscription().getNumberOfDaysAfter() || differenceAfterInWeeks == clientSubscription.getSubscription().getNumberOfWeeksAfter() || differenceAfterInMonths == clientSubscription.getSubscription().getNumberOfMonthsAfter()){

                        if(differenceAfterInDays == clientSubscription.getSubscription().getNumberOfDaysAfter()){
                            placeholders.put("daysAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfDaysAfter()));
                            placeholders.put("period", "days");
                        }else if(differenceAfterInMonths == clientSubscription.getSubscription().getNumberOfWeeksAfter()){
                            placeholders.put("weeksAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfWeeksAfter()));
                            placeholders.put("period", "weeks");
                        }
                        else if(differenceAfterInMonths == clientSubscription.getSubscription().getNumberOfMonthsAfter()){
                            placeholders.put("monthsAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfMonthsAfter()));
                            placeholders.put("period", "months");
                        }
                        else{
                            System.out.println("No reminder to be sent");
                        }
                        emailSubject = getEmailTemplateSubject(TemplateType.REMINDER_AFTER_EXPIRY);
                        emailMessage = getEmailTemplateMessage(TemplateType.REMINDER_AFTER_EXPIRY);
                        placeholders.put("fullName", clientSubscription.getClient().getClientFirstName() +" "+ clientSubscription.getClient().getClientLastName());
                        placeholders.put("SubscriptionName", clientSubscription.getSubscription().getSubscriptionName()); // Replace with actual data
                        placeholders.put("SubscriptionExpiryDate", clientSubscription.getSubscriptionEndDate().toString()); // Replace with actual data
                        EmailSetup(null, emailMessage, emailSubject, recipientEmail);
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