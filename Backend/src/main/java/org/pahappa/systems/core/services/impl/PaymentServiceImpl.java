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
    public Payment saveInstance(Payment payment) throws ValidationFailedException, OperationFailedException {
        try {
            //changing the invioce status
            System.out.println("We are changing statuses");

            //I want to first get the sum of all the payments made for a particular invoice keeping in mind they maynot be any
            //payments made for the invoice plus the payment that has just been made
            Double totalAmountPaid = 0.0;
            List<Payment> payments = this.getAllPaymentsOfParticularInvoice(payment.getInvoice().getId());
            for(Payment payment1: payments){
                totalAmountPaid += payment1.getAmountPaid();
            }

            //adding the current payment to the total amount paid
            totalAmountPaid += payment.getAmountPaid();

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

                clientAccount.setBalance(currentAccountBalance + balance);

                this.clientAccountService.updateClientAccount(clientAccount);
            }

            savedPayment = save(payment);
            return savedPayment;

        } catch (Exception e) {
            throw new OperationFailedException("Failed to save payment.", e);
        }
    }

//    public List<Payment> getPaymentsWithPendingApprovalInvoices(){
//        Search search = new Search();
//        if(!currentUser.hasAdministrativePrivileges()){
//            search.addFilterEqual("createdBy", currentUser);
//        }
//        search.addFilterEqual("invoice.invoiceStatus", InvoiceStatus.PENDING_APPROVAL);
//        return super.search(search);
//    }

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

    @Override
    public boolean isDeletable(Payment instance) throws OperationFailedException {
        return false;
    }
}
