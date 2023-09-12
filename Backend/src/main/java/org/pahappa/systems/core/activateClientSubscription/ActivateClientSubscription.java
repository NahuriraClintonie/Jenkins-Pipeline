package org.pahappa.systems.core.activateClientSubscription;

import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class ActivateClientSubscription {
    private Timer scheduledTaskExecuterTimer;
    @PostConstruct
    public void init(){
       scheduledTaskExecuterTimer = new Timer(6000, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               LocalDate currentDate = LocalDate.now().minusMonths(1);
               Date subscriptionStartDate = Date.from((currentDate.plusMonths(1)).atStartOfDay(ZoneId.systemDefault()).toInstant());
               List<Invoice> invoices = ApplicationContextProvider.getBean(InvoiceService.class).getInvoiceByStatusPaid(subscriptionStartDate);
               if (invoices == null) {
                   System.out.println("Active null");
               } else {
                   for (Invoice invoice : invoices) {
                       ClientSubscription clientSubscription = ApplicationContextProvider.getBean(ClientSubscriptionService.class).getClientSubscriptionById(invoice.getClientSubscription().getId());
                       ApplicationContextProvider.getBean(ClientSubscriptionService.class).activateClientSubscription(clientSubscription);
                   }
               }
           }
       }) ;
        scheduledTaskExecuterTimer.setInitialDelay(60000);
        scheduledTaskExecuterTimer.start();
    }
}
