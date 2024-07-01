package org.pahappa.systems.core.services;
//imports
import com.googlecode.genericdao.search.Search;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InvoiceService extends GenericService<Invoice> {

    void changeStatusToPaid(Invoice invoice, double amount);

    void changeStatusToUnpaid(Invoice instance);

    void changeStatusToPartiallyPaid(Invoice invoice, double amount) throws ValidationFailedException, OperationFailedException;

    byte[] generateInvoicePdf(Invoice invoice, PaymentTerms paymentTerms, Optional<String> invoiceTitle);

     List<Invoice> getInvoiceByStatus(Search search);

    List<Invoice> getInvoiceByClientSubscriptionId(List<ClientSubscription> clientSubscriptions);

}
