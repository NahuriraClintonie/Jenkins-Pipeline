package org.pahappa.systems.core.services.impl;
//imports

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.clientAccount.ClientAccount;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.ClientAccountService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;


@Transactional
@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {
    Payment savedPayment = null;
    private InvoiceService invoiceService;

    private ClientAccountService clientAccountService;

    @PostConstruct
    public void init(){
        this.invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        this.clientAccountService = ApplicationContextProvider.getBean(ClientAccountService.class);
    }

    @Override
    public Payment saveInstance(Payment payment){
        return save(payment);
    }

    public List<Payment> getAllPaymentsOfParticularInvoice(String invoiceId){
        Search search = new Search();
        search.addFilterEqual("invoice.id",invoiceId);
        search.addSortDesc("dateCreated");
        return super.search(search);
    }

    @Override
    public List<Payment> returnAllRequiredPayments(Search search) {
        return super.search(search);
    }

    public Payment updatePaymentStatus(Payment payment) throws ValidationFailedException, OperationFailedException {

        Double totalAmountPaid = 0.0;
        List<Payment> payments = this.getAllPaymentsOfParticularInvoice(payment.getInvoice().getId());
        for(Payment payment1: payments){
            if (payment1.getStatus().equals(PaymentStatus.APPROVED)){
                totalAmountPaid += payment1.getAmountPaid();
            }
        }

        //checking if the total amount paid is equal to the invoice total amount
        if(totalAmountPaid == payment.getInvoice().getInvoiceTotalAmount()){
            //change invoice status to paid
            this.invoiceService.changeStatusToPaid(payment.getInvoice(), payment.getAmountPaid());
        }
        else if(totalAmountPaid < payment.getInvoice().getInvoiceTotalAmount()){
            //changing the invoice status to partially paid
            this.invoiceService.changeStatusToPartiallyPaid(payment.getInvoice(), payment.getAmountPaid());
        }else if (totalAmountPaid > payment.getInvoice().getInvoiceTotalAmount()){
            //change invoice status to paid
            this.invoiceService.changeStatusToPaid(payment.getInvoice(), payment.getAmountPaid());

            Double balance = totalAmountPaid - payment.getInvoice().getInvoiceTotalAmount();

            //creating a search for the particular client account
            Search search = new Search();

            search.addFilterEqual("clientId", payment.getInvoice().getClientSubscription().getClient());

            //get the particular client account
            ClientAccount clientAccount = this.clientAccountService.getParticularClientAccount(search);

            Double currentAccountBalance = clientAccount.getBalance();

            System.out.println("Current account balence"+ currentAccountBalance);
            System.out.println("original balence"+ balance);
            System.out.println("New balence"+ (balance+currentAccountBalance));

            clientAccount.setBalance(currentAccountBalance + balance);

            this.clientAccountService.updateClientAccount(clientAccount);
        }

        savedPayment = save(payment);
        return savedPayment;
    }

    @Override
    public boolean isDeletable(Payment instance) throws OperationFailedException {
        return false;
    }
}
