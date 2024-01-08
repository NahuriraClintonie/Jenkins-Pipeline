package org.pahappa.systems.core.services.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Transactional
@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {

    Payment savedPayment = null;

    private InvoiceService invoiceService;

    private ApplicationEmailService applicationEmailService;

    private ClientSubscriptionService clientSubscriptionService;

    @PostConstruct
    public void init(){
        this.applicationEmailService= ApplicationContextProvider.getBean(ApplicationEmailService.class);
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
                    System.out.println("changing status to paid");
                    this.invoiceService.changeStatusToPaid(payment.getInvoice(), payment.getAmountPaid());

                }
                else if(payment.getAmountPaid() < payment.getInvoice().getInvoiceTotalAmount()){
//                    this.invoiceService.changeStatusToPartiallyPaid(payment.getInvoice(), payment.getAmountPaid());
                    this.invoiceService.changeStatusToPartiallyPaid(payment.getInvoice(), payment.getInvoice().getInvoiceTotalAmount() - payment.getAmountPaid());
//                    double newInvoiceAmount = payment.getInvoice().getInvoiceTotalAmount() - payment.getAmountPaid();
//                    payment.getInvoice().setInvoiceBalance(newInvoiceAmount);
//                    applicationEmailService.saveBalanceInvoice(payment.getInvoice(), "Invoice for Balances");
                }

                savedPayment = save(payment);
                PaymentService.generateReceipt(savedPayment);
                applicationEmailService.saveReciept(savedPayment, "Payment Receipt");

            }

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

    @Override
    public boolean isDeletable(Payment instance) throws OperationFailedException {
        return false;
    }
}
