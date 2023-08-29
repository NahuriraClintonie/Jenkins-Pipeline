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
import java.time.LocalDate;
import java.time.ZoneId;
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
        LocalDate currentDate = LocalDate.now().minusMonths(2);
        Date subscriptionEndDate = Date.from((currentDate.plusMonths(2)).atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.clientSubscriptions = ApplicationContextProvider.getBean(ClientSubscriptionService.class).getClientSubscriptionsByEndDate(subscriptionEndDate);
      CustomLogger.log(String.valueOf(clientSubscriptions.size()));

        scheduledTaskExecuterTimer = new Timer(60000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CustomLogger.log("Executing task at: " + new Date());
                sendClientReminder(); // Call your method here
            }
        });

        // Set initial delay and start the Timer
        scheduledTaskExecuterTimer.setInitialDelay(60000);
        scheduledTaskExecuterTimer.start();
    }



    public void sendClientReminder() {

//        LocalDate currentDate = LocalDate.now();
//        LocalDate target = LocalDate.of(2023,8,30 );

        for(ClientSubscription clientSubscription:clientSubscriptions){
            System.out.println(clientSubscription.getClient().getClientEmail());
        String recipientEmail=clientSubscription.getClient().getClientEmail();
        String firstName = clientSubscription.getClient().getClientFirstName();
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
            message.setFrom(new InternetAddress("caden.wwdd@gmail.com", "Pahappa Invoice Payment Reminder"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Kimwanyi SACCO Memberhip Approval");
            message.setText("Dear Daniella \n\n" +
                    "We are delighted to inform you that your Memberhip application to Kimwanyi SACCO has been approved!\n\n"
                    +
                    "Here are your login credentials:\n\n" +
                    "Username: " + recipientEmail + "\n" +
                    "Temporary Password: " + recipientEmail + "\n\n" +
                    "Please use the provided credentials to log in to your account. For security purposes, we recommend that you change your password after your first login.\n\n"
                    +
                    "Thank you for joining Kimwanyi SACCO. We look forward to serving you.\n\n" +
                    "Best regards,\n" +
                    "Kimwanyi SACCO Team");

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
