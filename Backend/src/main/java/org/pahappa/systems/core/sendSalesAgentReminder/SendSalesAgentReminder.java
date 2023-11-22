package org.pahappa.systems.core.sendSalesAgentReminder;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.salesAgentReminder.SalesAgentReminder;
import org.pahappa.systems.core.services.SalesAgentReminderService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import java.util.Date;

@Getter
@Setter
public class SendSalesAgentReminder {

    private  SalesAgentReminderService salesAgentReminderService;
    private  SalesAgentReminder salesAgentReminder = new SalesAgentReminder();

    public void init(){
        this.salesAgentReminderService = ApplicationContextProvider.getBean(SalesAgentReminderService.class);
    }
    public void sendSalesAgentReminder(Invoice invoice){
        init();
        System.out.println("Reminder"+invoice.getClientSubscription().getClient().getClientEmail());
        Date date = new Date();
        salesAgentReminder.setInvoice(invoice);
        salesAgentReminder.setMessage("An invoice," + invoice.getInvoiceNumber() + " due date," + invoice.getInvoiceDueDate() + " has been to sent to"+ invoice.getClientSubscription().getClient().getClientFirstName()+" " + invoice.getClientSubscription().getClient().getClientLastName() + "phone number:" + invoice.getClientSubscription().getClient().getClientContact());
        salesAgentReminder.setIsRead(false);
        salesAgentReminder.setUser(invoice.getClientSubscription().getClient().getCreatedBy());
        salesAgentReminder.setSentDate(date);
        if(salesAgentReminder==null){
            System.out.println("null");
        }
        else{
            System.out.println("Sales agent is not null");
            try {
               salesAgentReminderService.saveInstance(salesAgentReminder);
            } catch (ValidationFailedException e) {
                throw new RuntimeException(e);
            } catch (OperationFailedException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
