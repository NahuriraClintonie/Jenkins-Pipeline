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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Transactional
@Service
public class ApplicationEmailServiceImpl extends GenericServiceImpl<AppEmail> implements ApplicationEmailService {

    static boolean locked= false;
    private Invoice invoiceObject;

    private Payment paymentObject;

    private EmailSetupService emailSetupService;

    private EmailSetup emailSetup;

    private ClientSubscriptionService clientSubscriptionService;

    @PostConstruct
    public void init(){
      emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        emailSetup = emailSetupService.getActiveEmail();
    }

    public void setClientInvoices(List<Invoice> clientInvoices) {
        this.clientInvoices = clientInvoices;
    }

    @Getter
    private List<Invoice> clientInvoices;

    private InvoiceService invoiceService;

    private List<ClientSubscription> clientSubscriptionsList;






    private Invoice invoice;

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

    private void EmailSetup(Object object, String emailSubject) {
        AppEmail appEmail = new AppEmail();
        String recipientEmail;

        if(Invoice.class.isInstance(object)){
            this.invoiceObject = (Invoice) object;
            appEmail.setInvoiceObject(invoiceObject);
            recipientEmail = invoiceObject.getClientSubscription().getClient().getClientEmail();
        }else{
            this.paymentObject= (Payment) object;
            appEmail.setPaymentObject(this.paymentObject);
            recipientEmail = paymentObject.getInvoice().getClientSubscription().getClient().getClientEmail();
        }

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

    public void saveBalanceInvoice(Invoice invoiceObject, String emailSubject){
        EmailSetup(invoiceObject, emailSubject);

    }

    public void saveReciept(Payment paymentObject, String emailSubject){
        EmailSetup(paymentObject,emailSubject);
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

    public void sendEmail(String recipientEmail, String subject, String messageToSend, Object object) {

        String filePath;

        if (Invoice.class.isInstance(object)){

            InvoiceService.generateInvoicePdf((Invoice) object);
            filePath = "E:\\Pahappa Documents\\automated-invoicing\\Invoice.pdf";
            System.out.println("we are done generating");
        }else{
            PaymentService.generateReceipt((Payment) object);
            filePath = "E:\\Pahappa Documents\\automated-invoicing\\Receipt.pdf";
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
            message.setText("Dear Client,\n\nPlease find the attached invoice.\n\nBest Regards,\nPahappa Limited");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            messageBodyPart.attachFile(filePath);
            multipart.addBodyPart(messageBodyPart);

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
            ex.printStackTrace();
        }
    }

    public void sendClientReminder(){
        if(!locked){
            locked=true;
            invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
            clientInvoices = invoiceService.getInvoiceByStatus();
            // getInvoiceByStatus returns all invoices that are unpaid and partially paid
            System.out.println(clientInvoices.size());

            if(clientInvoices.isEmpty()){
                System.out.println("No unpaid client invoices");
            }else{
                for(Invoice clientInvoice: clientInvoices) {
                    System.out.println("Invoice Client reminder:" + clientInvoice.getClientSubscription().getClient().getClientEmail());

                    //check if there is reminder with the same invoice number and has a status not sent in the appEmail table

                    Search search = new Search();
                    search.addFilterEqual("invoiceObject.invoiceNumber", clientInvoice.getInvoiceNumber());
                    search.addFilterEqual("emailStatus", false);
                    List<AppEmail> appEmails = super.search(search);

                    if (appEmails.isEmpty()) {
                        if (clientInvoice.getInvoiceStatus() == InvoiceStatus.UNPAID){
                            System.out.println("No reminder with the same invoice number");
                            //Check if the current date is 10, 5, 2 days from the invoice due date
                            Date date = new Date();
                            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            int currentDay = localDate.getDayOfMonth();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(clientInvoice.getInvoiceDueDate());
                            int dueDate = cal.get(Calendar.DAY_OF_MONTH);
//                            int difference = dueDate - currentDay;
//                            System.out.println("Difference from the due date from the invoice:" + difference);
//                            if (difference == 10 || difference == 5 || difference == 2) {
//                                System.out.println("Difference is 10, 5 or 2");
//                                saveInvoice(clientInvoice, "Invoice Payment Reminder for Invoice "+ clientInvoice.getInvoiceNumber());
//                            }

                            int difference = dueDate - currentDay;

                            if(difference == clientInvoice.getClientSubscription().getNumberOfDaysBeforeOverDueDate()){
                                saveInvoice(clientInvoice, "Invoice Payment Reminder for Invoice "+ clientInvoice.getInvoiceNumber());
                            }

                            int afterOverDueDifference = currentDay - dueDate;
                            if (afterOverDueDifference == clientInvoice.getClientSubscription().getNumberOfDaysAfterOverDueDate()){
                                saveInvoice(clientInvoice, "OverDue Payment Reminder for Invoice "+ clientInvoice.getInvoiceNumber());
                            }
                        }

                    }
                    else {
                        System.out.println("Reminder with the same invoice number hasn't yet been sent");
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
