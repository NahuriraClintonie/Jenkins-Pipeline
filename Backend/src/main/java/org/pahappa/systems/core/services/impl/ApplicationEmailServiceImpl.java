package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.appEmail.AppEmail;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
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

    private ClientSubscriptionService clientSubscriptionService;

    public void setClientInvoices(List<Invoice> clientInvoices) {
        this.clientInvoices = clientInvoices;
    }

    @Getter
    private List<Invoice> clientInvoices;

    private InvoiceService invoiceService;


    private Invoice invoice;
    private PaymentTermsServiceImpl paymentTermsService;

    @Override
    public AppEmail saveInstance(AppEmail entityInstance) throws ValidationFailedException, OperationFailedException {

        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(AppEmail instance) throws OperationFailedException {
        return false;
    }

    public void saveInvoice(Invoice invoiceObject, String emailSubject){
        this.invoiceObject = invoiceObject;

        AppEmail appEmail = new AppEmail();

        String recipientEmail = invoiceObject.getClientSubscription().getClient().getClientEmail();

        appEmail.setSenderEmail("caden.wwdd@gmail.com");

        appEmail.setSenderPassword("tlipzljibdhzptke");

        appEmail.setReceiverEmail(recipientEmail);

        appEmail.setEmailSubject(emailSubject);

        appEmail.setInvoiceObject(invoiceObject);

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
        this.invoiceObject = invoiceObject;

        AppEmail appEmail = new AppEmail();

        String recipientEmail = invoiceObject.getClientSubscription().getClient().getClientEmail();

        appEmail.setSenderEmail("caden.wwdd@gmail.com");

        appEmail.setSenderPassword("tlipzljibdhzptke");

        appEmail.setReceiverEmail(recipientEmail);

        appEmail.setEmailSubject(emailSubject);

        appEmail.setInvoiceObject(invoiceObject);

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

    public void saveReciept(Payment paymentObject, String emailSubject){
        this.paymentObject = paymentObject;

        AppEmail appEmail = new AppEmail();

        String recipientEmail = paymentObject.getInvoice().getClientSubscription().getClient().getClientEmail();

        appEmail.setSenderEmail("caden.wwdd@gmail.com");

        appEmail.setSenderPassword("tlipzljibdhzptke");

        appEmail.setReceiverEmail(recipientEmail);

        appEmail.setEmailSubject(emailSubject);

        appEmail.setPaymentObject(paymentObject);

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

    public void sendEmail(String recipientEmail, String subject, String messageToSend, Object object) {

        String filePath;

        if (Invoice.class.isInstance(object)){


            InvoiceService.generateInvoicePdf((Invoice) object,paymentTermsService.getAllInstances().stream().findFirst().orElse(new PaymentTerms()));
            filePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/automated-invoicing/Invoice.pdf";


            System.out.println("we are done generating");
        }else{
            PaymentService.generateReceipt((Payment) object);
            filePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/automated-invoicing/Invoice.pdf";
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Set up the session with the authentication details
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("caden.wwdd@gmail.com", "tlipzljibdhzptke");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("caden.wwdd@gmail.com", "Pahappa Limited"));
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
            invoiceService =ApplicationContextProvider.getBean(InvoiceService.class);
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
                            int difference = dueDate - currentDay;
                            System.out.println("Difference from the due date from the invoice:" + difference);
                            if (difference == 10 || difference == 5 || difference == 2) {
                                System.out.println("Difference is 10, 5 or 2");
                                saveInvoice(clientInvoice, "Invoice Payment Reminder for Invoice "+ clientInvoice.getInvoiceNumber());
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

}
