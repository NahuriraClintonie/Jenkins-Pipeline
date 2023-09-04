package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.constants.PaymentMethod;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;


@Transactional
@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {

    Payment savedPayment = null;
    private InvoiceService invoiceService;
    @PostConstruct
    public void init(){
        this.invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
    }

    @Override
    public Payment saveInstance(Payment payment) throws ValidationFailedException, OperationFailedException {
        try {

            if(payment.getStatus().equals(PaymentStatus.PENDING)){
                this.invoiceService.changeStatusToPendingApproval(payment.getInvoice());
                savedPayment = save(payment);
            }
            else if(payment.getStatus().equals(PaymentStatus.APPROVED)){

                if(payment.getAmountPaid() == payment.getInvoice().getInvoiceTotalAmount()) {
                    this.invoiceService.changeStatusToPaid(payment.getInvoice(), payment.getAmountPaid());
                }
                else {
                    this.invoiceService.changeStatusToPartiallyPaid(payment.getInvoice(), payment.getAmountPaid());
                }

                savedPayment = save(payment);
            }

//            payment.setStatus(PaymentStatus.PENDING);
//            Payment savedPayment = save(payment);

            return savedPayment;
        } catch (Exception e) {
            throw new OperationFailedException("Failed to save payment.", e);
        }
    }

    @Override
    public boolean isDeletable(Payment instance) throws OperationFailedException {
        return false;
    }
    
}
