package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

public interface ApplicationEmailService extends GenericService<AppEmail> {
    public void saveInvoice(Invoice invoiceObject, String subject);

    public void saveBalanceInvoice(Invoice invoiceObject, String subject);

    public void sendSavedInvoices();

    public void sendClientReminder();

    public void saveReciept(Payment paymentObject, String emailSubject);

    public void generateInvoiceForNewClientSubscription();
}
