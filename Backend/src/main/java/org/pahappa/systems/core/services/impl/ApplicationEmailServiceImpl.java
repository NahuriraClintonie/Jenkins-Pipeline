package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.constants.TemplateType;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.models.appEmail.EmailsToCc;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.emailTemplate.EmailTemplate;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.sendSalesAgentReminder.SendSalesAgentReminder;
import org.pahappa.systems.core.services.*;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.sers.webutils.server.shared.SharedAppData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class ApplicationEmailServiceImpl extends GenericServiceImpl<AppEmail> implements ApplicationEmailService {

    static boolean locked= false;

    private Invoice invoiceObject;

    byte[] pdfBytes;

    byte[] pdfContent;

    File pdfFile;

    private ClientSubscriptionService clientSubscriptionService;

    private InvoiceService invoiceService;

    Map<String, String> placeholders = new HashMap<>();

    private PaymentTermsService paymentTermsService;

    private EmailSetupService emailSetupService;

    private EmailSetup emailSetup;

    private List<ClientSubscription> clientSubscriptionsList;

    private EmailsToCcService emailsToCcService;

    private EmailTemplateService emailTemplateService;
    private EmailTemplate emailTemplate;
    private String emailSubject;
    private String emailMessage;
    private String updatedEmailMessage;

    private String recipientEmail;
    private User currentUser;


    @PostConstruct
    public void init(){
        paymentTermsService = ApplicationContextProvider.getBean(PaymentTermsService.class);
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);
        emailsToCcService = ApplicationContextProvider.getBean(EmailsToCcService.class);
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
        if(Invoice.class.isInstance(invoiceObject)){
            this.invoiceObject = invoiceObject;

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

    private void EmailSetup(Invoice invoiceObject, String emailMessage, String emailSubject, String recipientEmail) {
        AppEmail appEmail = new AppEmail();

        if(invoiceObject != null){
            appEmail.setInvoiceObject(invoiceObject);
            appEmail.setAutoSendStatusAppEmail(invoiceObject.getClientSubscription().getClient().getAutoSendStatus());
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

    //background process
    public void sendSavedInvoices(){
        if(!locked){
            locked =true;
            System.out.println("\n\n\nStarting send emails\n\n\n");
            Search search = new Search();
            search.addFilterEqual("emailStatus", false)
                    .addFilterEqual("autoSendStatusAppEmail", true);
            List<AppEmail> appEmails = super.search(search);
            System.out.println("Getting emails to send\n\n\n");
            for(AppEmail appEmail: appEmails){
                try {

                    if(appEmail.getInvoiceObject() == null){
                        List<EmailsToCc> emailsToCcList = null;
                        //Here we send the recepit us
                        sendEmail(appEmail.getReceiverEmail(), appEmail.getEmailSubject(), appEmail.getEmailMessage(), null, emailsToCcList);
                    }else {
                        List<EmailsToCc> emailsToCcList = emailsToCcService.getByClientSubscritpion(appEmail.getInvoiceObject().getClientSubscription().getId());
                        System.out.println("The search for emails to cc returned "+ emailsToCcList.size());
                        if (appEmail.getInvoiceObject().getClientSubscription().getClient().getAutoSendStatus()) {
                            System.out.println("The invoice auto send status is: "+appEmail.getInvoiceObject().getClientSubscription().getClient().getAutoSendStatus());
                            sendEmail(appEmail.getReceiverEmail(), appEmail.getEmailSubject(), appEmail.getEmailMessage(), appEmail.getInvoiceObject(), emailsToCcList);
                        } else {
                            System.out.println("The invoice can't be sent coz it's auto-send status is: "+appEmail.getInvoiceObject().getClientSubscription().getClient().getAutoSendStatus());
                        }
                    }
                    appEmail.setAutoSendStatusAppEmail(false);
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

    @Override
    public List<AppEmail> getParticularSalesAgentEmails(Search search) {
        search.addSortDesc("dateCreated");
        return super.search(search);
    }

    public void sendEmail(String recipientEmail, String subject, String messageToSend, Object object, List<EmailsToCc> emailsToCcList) throws IOException {
        emailSetup = emailSetupService.getActiveEmail();
        String filePath;
//        byte[] pdfBytes;

        if (Invoice.class.isInstance(object)){
            System.out.println("Smtp Host is: "+emailSetup.getSmtpHost());
            System.out.println(object.getClass().getName());
            System.out.println("The Account Name is: "+paymentTermsService.getAllInstances().stream().findFirst().orElse(new PaymentTerms()).getAccountName());

            pdfBytes = invoiceService.generateInvoicePdf((Invoice) object,paymentTermsService.getAllInstances().stream().findFirst().orElse(new PaymentTerms()),Optional.of("Taxed Invoice"));
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
                if (emailsToCcList != null && !emailsToCcList.isEmpty()) {
                    List<InternetAddress> ccAddressesList = new ArrayList<>();
                    for (EmailsToCc cc : emailsToCcList) {
                        try {
                            String emailAddress = cc.getEmailAddress();
                            System.out.println("Processing email: " + emailAddress);
                            ccAddressesList.add(new InternetAddress(emailAddress));
                        } catch (AddressException e) {
                            // Handle the invalid email address (e.g., log it and continue)
                            System.err.println("Invalid email address: " + cc.getEmailAddress());
                        }
                    }

                    // Convert list to array
                    InternetAddress[] ccAddresses = ccAddressesList.toArray(new InternetAddress[0]);
                    System.out.println("CC addresses count: " + ccAddresses.length);

                    try {
                        message.setRecipients(Message.RecipientType.CC, ccAddresses);
                        System.out.println("CC recipients set successfully.");
                    } catch (MessagingException e) {
                        System.out.println("MessagingException: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("emailsToCcList is null or empty.");
                }

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


    //background process
    public void sendClientReminder(){
        if(!locked){
            CustomLogger.log("\nApplicationEmailServiceImpl-sendClientReminder: The reminder method is starting to be executed\n\n");
            locked=true;
            clientSubscriptionsList = clientSubscriptionService.getClientSubscriptionsThatAreNotInActive();
            SendSalesAgentReminder reminder = new SendSalesAgentReminder();
            if(clientSubscriptionsList.isEmpty()){
                System.out.println("No client subscriptions");
            }else{
                LocalDate currentDate = LocalDate.now();
                for(ClientSubscription clientSubscription: clientSubscriptionsList){
                    recipientEmail = clientSubscription.getClient().getClientEmail();
                    placeholders.put("fullName", clientSubscription.getClient().getClientFirstName() + " " + clientSubscription.getClient().getClientLastName());
                    placeholders.put("SubscriptionName", clientSubscription.getSubscription().getSubscriptionName()); // Replace with actual data
                    placeholders.put("SubscriptionExpiryDate", clientSubscription.getSubscriptionEndDate().toString()); // Replace with actual data

                    if (clientSubscription.getDateForDailyReminderBeforeDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate) || clientSubscription.getDateForWeeklyReminderBeforeDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate) || clientSubscription.getDateForMonthlyReminderBeforeDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)){
                        if(clientSubscription.getDateForDailyReminderBeforeDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)){
                            placeholders.put("numberOfDays/months/yearsToOrAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfDaysBefore()));
                            placeholders.put("period", "days");
                        }
                        else if(clientSubscription.getDateForWeeklyReminderBeforeDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)){
                            placeholders.put("numberOfDays/months/yearsToOrAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfWeeksBefore()));
                            placeholders.put("period", "weeks");
                        }
                        else if(clientSubscription.getDateForMonthlyReminderBeforeDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)){
                            placeholders.put("numberOfDays/months/yearsToOrAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfMonthsBefore()));
                            placeholders.put("period", "months");
                        }
                        else {
                            CustomLogger.log("\nApplicationEmailServiceImpl-sendClientReminder: Didn't satisfy any of the statements for Reminders Before Due Date");
                        }
                        emailSubject = getEmailTemplateSubject(TemplateType.REMINDER_BEFORE_EXPIRY);
                        emailMessage = getEmailTemplateMessage(TemplateType.REMINDER_BEFORE_EXPIRY);
                        EmailSetup(null, replacePlaceholders(emailMessage,placeholders), emailSubject, recipientEmail);
                        reminder.saveSalesAgentReminder(clientSubscription, replacePlaceholders(emailMessage,placeholders));
                    }
                    else if(clientSubscription.getDateForDailyReminderAfterDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate) || clientSubscription.getDateForWeeklyReminderAfterDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate) || clientSubscription.getDateForMonthlyReminderAfterDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)) {
                        if (clientSubscription.getDateForDailyReminderAfterDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)) {
                            placeholders.put("numberOfDays/months/yearsToOrAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfDaysAfter()));
                            placeholders.put("period", "days");
                        } else if (clientSubscription.getDateForWeeklyReminderAfterDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)) {
                            placeholders.put("numberOfDays/months/yearsToOrAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfWeeksAfter()));
                            placeholders.put("period", "weeks");
                        } else if (clientSubscription.getDateForMonthlyReminderAfterDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)) {
                            placeholders.put("numberOfDays/months/yearsToOrAfterExpiry", String.valueOf(clientSubscription.getSubscription().getNumberOfMonthsAfter()));
                            placeholders.put("period", "months");
                        } else {
                            CustomLogger.log("\nApplicationEmailServiceImpl-sendClientReminder: Didn't satisfy any of the statements for Reminders After Due Date");
                        }
                        emailSubject = getEmailTemplateSubject(TemplateType.REMINDER_AFTER_EXPIRY);
                        emailMessage = getEmailTemplateMessage(TemplateType.REMINDER_AFTER_EXPIRY);
                        EmailSetup(null, emailMessage, emailSubject, recipientEmail);
                        reminder.saveSalesAgentReminder(clientSubscription, replacePlaceholders(emailMessage, placeholders));
                    }
                    else{
                        System.out.println("No reminder to be sent");
                    }

                }
            }

            CustomLogger.log("\nApplicationEmailServiceImpl-sendClientReminder: Reminder method execution complete\n\n");

            locked=false;
        }
    }


    public void sendActivationOrDeactivationReminders(ClientSubscription clientSubscription){
        SendSalesAgentReminder reminder = new SendSalesAgentReminder();
        recipientEmail = clientSubscription.getClient().getClientEmail();
        placeholders.put("fullName", clientSubscription.getClient().getClientFirstName() + " " + clientSubscription.getClient().getClientLastName());
        placeholders.put("SubscriptionName", clientSubscription.getSubscription().getSubscriptionName()); // Replace with actual data

        if(clientSubscription.getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE)){
            emailSubject = getEmailTemplateSubject(TemplateType.REMINDER_ON_ACTIVATION);
            emailMessage = getEmailTemplateMessage(TemplateType.REMINDER_ON_ACTIVATION);
            EmailSetup(null, emailMessage, emailSubject, recipientEmail);
            reminder.saveSalesAgentReminder(clientSubscription, replacePlaceholders(emailMessage, placeholders));
        }else if(clientSubscription.getSubscriptionStatus().equals(SubscriptionStatus.INACTIVE)){
            emailSubject = getEmailTemplateSubject(TemplateType.REMINDER_ON_DEACTIVATION);
            emailMessage = getEmailTemplateMessage(TemplateType.REMINDER_ON_DEACTIVATION);
            EmailSetup(null, emailMessage, emailSubject, recipientEmail);
            reminder.saveSalesAgentReminder(clientSubscription, replacePlaceholders(emailMessage, placeholders));
        }else{
            CustomLogger.log("\nApplicationEmailServiceImpl-sendActivationOrDeactivationReminders: Subscription status is not active or inactive\n\n");
        }
    }

    //background process
    public void createNewClientSubscription() {
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
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