package org.pahappa.systems.core.services;
//imports
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

public interface ApplicationEmailService extends GenericService<AppEmail> {

    void saveInvoice(Invoice invoiceObject);

    void sendSavedInvoices();

    void sendClientReminder();

    void createNewClientSubscription();

    public void sendActivationOrDeactivationReminders(ClientSubscription clientSubscription);
}
