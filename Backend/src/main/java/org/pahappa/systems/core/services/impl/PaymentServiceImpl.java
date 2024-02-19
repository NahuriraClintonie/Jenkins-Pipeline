package org.pahappa.systems.core.services.impl;
//imports
import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;


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
            //changing the invioce status
            if(payment.getStatus().equals(PaymentStatus.PENDING)){
                //changing the invoice status to pending approval
                this.invoiceService.changeStatusToPendingApproval(payment.getInvoice());
                savedPayment = save(payment);
            }
            else if(payment.getStatus().equals(PaymentStatus.APPROVED)){
                if(payment.getAmountPaid() == payment.getInvoice().getInvoiceTotalAmount()) {
                    //change invoice status to paid
                    this.invoiceService.changeStatusToPaid(payment.getInvoice(), payment.getAmountPaid());
                }
                else if(payment.getAmountPaid() < payment.getInvoice().getInvoiceTotalAmount()){
                    //changing the invoice status to partially paid
                    this.invoiceService.changeStatusToPartiallyPaid(payment.getInvoice(), payment.getAmountPaid());
                }
            }
            else{
                CustomLogger.log("PaymentServiceImpl-saveInstance: Status update complete");
            }
            savedPayment = save(payment);
            return savedPayment;

        } catch (Exception e) {
            throw new OperationFailedException("Failed to save payment.", e);
        }
    }

    public List<Payment> getPaymentsWithPendingApprovalInvoices(){
        Search search = new Search();
        search.addFilterEqual("invoice.invoiceStatus", InvoiceStatus.PENDING_APPROVAL);
        return super.search(search);
    }

    public List<Payment> getAllPaymentsOfParticularInvoice(String invoiceId){
        Search search = new Search();
        search.addFilterEqual("invoice.id",invoiceId);
        return super.search(search);
    }

    @Override
    public boolean isDeletable(Payment instance) throws OperationFailedException {
        return false;
    }
}
