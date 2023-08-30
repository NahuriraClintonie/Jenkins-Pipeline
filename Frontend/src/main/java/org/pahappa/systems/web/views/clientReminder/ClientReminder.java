package org.pahappa.systems.web.views.clientReminder;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class ClientReminder {
    private Timer scheduledTaskExecuterTimer;
    private ClientSubscriptionService clientSubscriptionService;
    private List<ClientSubscription> clientSubscriptions;
    private Search search;
    private String searchTerm;

    private List<SearchField> searchFields;

    @PostConstruct
    public void init(){

        scheduledTaskExecuterTimer = new Timer(600000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                LocalDate currentDate = LocalDate.now().minusMonths(2);
                Date subscriptionEndDate = Date.from((currentDate.plusMonths(2)).atStartOfDay(ZoneId.systemDefault()).toInstant());
                clientSubscriptions = ApplicationContextProvider.getBean(ClientSubscriptionService.class).getClientSubscriptionsByEndDate(subscriptionEndDate);
                CustomLogger.log(String.valueOf(clientSubscriptions.size()));

                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                CustomLogger.log("Executing task at: " + new Date());
                sendClientReminder(); // Call your method here
            }
        });

        // Set initial delay and start the Timer
        scheduledTaskExecuterTimer.setInitialDelay(60000);
        scheduledTaskExecuterTimer.start();
    }



    public void sendClientReminder() {

        for(ClientSubscription clientSubscription:clientSubscriptions){
            System.out.println(clientSubscription.getClient().getClientEmail());
        String recipientEmail=clientSubscription.getClient().getClientEmail();
        String firstName = clientSubscription.getClient().getClientFirstName();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String subscriptionEndDate = dateFormat.format(clientSubscription.getSubscriptionEndDate());
        // Configure the email properties
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
            // Create a new message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("caden.wwdd@gmail.com", "Pahappa Limited"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Invoice Payment Reminder");
            message.setText("Dear " + firstName + ",\n\n" +
                    "We hope this email finds you well. We would like to remind you that your invoice is due for payment on" + subscriptionEndDate + ". We kindly request that you settle the outstanding amount at your earliest convenience.\n\n"
                    +
                    "For your convenience, we have attached a copy of the invoice to this email. You can review the details and payment options provided in the attached PDF.\n\n" +
                    "Please ensure that the payment is made by the due date to avoid your subscription being cancelled. If you have already made the payment, please disregard this reminder.\n" +
                    "If you have any questions, concerns, or require further assistance, please do not hesitate to contact our customer support team at info@pahappa.com or 07567884683/0778986784. \n\n" +
                    "Thank you for choosing Pahappa Limited's services. We greatly appreciate your business.\n\n"
                    +

                    "Best regards,\n" +
                    "Pahappa Limited Team");

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception if the email sending fails
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // Handle the exception if the email sending fails
        }
    } }

}
