package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.sendSalesAgentReminder.SendSalesAgentReminder;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
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

    private List<ClientSubscription> clientSubscriptions;

    private InvoiceService invoiceService;


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
            filePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/Invoice.pdf";
        }else{
            PaymentService.generateReceipt((Payment) object);
            filePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/Receipt.pdf";
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

            if(this.invoiceObject != null){
                reminder.sendSalesAgentReminder(this.invoiceObject);
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public static void generatePdfAndSendEmail(Invoice invoiceObject) {
//        InvoiceService.generateInvoicePdf(invoiceObject);
//
//        // Send the PDF invoice as an attachment via email
//        final String username = invoiceObject.getClientSubscription().getClient().getClientFirstName()+" "+invoiceObject.getClientSubscription().getClient().getClientLastName();; // Your email username
//        recipientEmail = invoiceObject.getClientSubscription().getClient().getClientEmail();; // Your email password
//
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
//
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("caden.wwdd@gmail.com", "tlipzljibdhzptke");
//                    }
//                });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("caden.wwdd@gmail.com", "Pahappa Limited"));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
//            message.setSubject("Invoice Payment Reminder");
//            message.setText("Dear Client,\n\nPlease find the attached invoice.\n\nBest Regards,\nPahappa Limited");
//
//            MimeBodyPart messageBodyPart = new MimeBodyPart();
//            Multipart multipart = new MimeMultipart();
//            String filePath = "/home/devclinton/Documents/Pahappa/automated-invoicing/Invoice.pdf";
//            messageBodyPart.attachFile(filePath);
//            multipart.addBodyPart(messageBodyPart);
//
//            message.setContent(multipart);
//
//            Transport.send(message);
//
//            System.out.println("Invoice sent successfully.");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }



    public void sendClientReminder(){
        if(!locked){
            locked=true;
            System.out.println("\n\n\nStarting client reminder\n\n\n");
            LocalDate currentDate = LocalDate.now().minusMonths(1);
            Date subscriptionEndDate = Date.from((currentDate.plusMonths(1)).atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println("End Date  "+subscriptionEndDate);
            clientSubscriptionService= ApplicationContextProvider.getBean(ClientSubscriptionService.class);
            clientSubscriptions = ApplicationContextProvider.getBean(ClientSubscriptionService.class).getAllInstances();
            invoiceService =ApplicationContextProvider.getBean(InvoiceService.class);
            System.out.println(clientSubscriptions.size());
            for(ClientSubscription clientSubscription:clientSubscriptions) {
                System.out.println("Leos Client reminder:" + clientSubscription.getClient().getClientEmail());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(clientSubscription.getSubscriptionEndDate());
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                Date newSubscriptionStartDate = calendar.getTime();
                ClientSubscription newClientSubscription = ApplicationContextProvider.getBean(ClientSubscriptionService.class).getClientSubscriptionByStartDate(newSubscriptionStartDate, clientSubscription.getClient().getId(), clientSubscription.getSubscription().getProduct().getId());
                System.out.println("\n\n\nnot null\n\n\n");
                invoice = invoiceService.getInvoiceByClientSubscriptionId(clientSubscription.getId());
                if (invoice.getInvoiceStatus() != InvoiceStatus.PAID) {
                    System.out.println("Invoice Client reminder:" + invoice.getClientSubscription().getClient().getClientEmail());
                    saveInvoice(invoice, "Invoice Payment Reminder"+invoice.getInvoiceNumber());
                } else {
                    System.out.println("Paid");
                }
            }
            locked=false;
        }
    }

}
