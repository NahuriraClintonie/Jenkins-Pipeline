package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface PaymentService extends GenericService<Payment> {
    List<Payment> getPaymentsWithPendingApprovalInvoices();
}
