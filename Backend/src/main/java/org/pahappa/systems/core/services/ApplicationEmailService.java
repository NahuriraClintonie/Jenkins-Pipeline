package org.pahappa.systems.core.services;
//imports
import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

import java.util.List;

public interface ApplicationEmailService extends GenericService<AppEmail> {

    void saveInvoice(Invoice invoiceObject);

    void sendActivationOrDeactivationReminders(ClientSubscription clientSubscription);

    void sendSavedInvoices();

    List<AppEmail> getParticularSalesAgentEmails(Search search);
}
