package org.pahappa.systems.core.services.impl;
//imports
import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.InvoiceTaxService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
//import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Service
@Transactional
public class InvoiceServiceImpl extends GenericServiceImpl<Invoice> implements InvoiceService {
    Date currentDate = new Date();

    Search search = new Search();

    private User loggedInUser = SharedAppData.getLoggedInUser();
    private InvoiceTaxService invoiceTaxService;

    private ApplicationEmailService applicationEmailService;

    private List<InvoiceTax> invoiceTaxList;

    private Invoice newInvoice;

    @PostConstruct
    public void init(){
        invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);
        applicationEmailService = ApplicationContextProvider.getBean(ApplicationEmailService.class);
    }


    @Override
    public Invoice saveInstance(Invoice entityInstance) throws ValidationFailedException, OperationFailedException {

        changeInvoiceNumber(entityInstance);

        if(entityInstance.getInvoiceStatus() == null){
            entityInstance.setInvoiceStatus(InvoiceStatus.UNPAID);
        }

        // Add a period of 15 days
        int daysToAdd = 15;

        //I want the due date to be the start date of the subscription + 15 days
        Calendar cal = Calendar.getInstance();
        cal.setTime(entityInstance.getClientSubscription().getSubscriptionStartDate());
        cal.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Get the updated date
        Date updatedDate = cal.getTime();
        entityInstance.setInvoiceDueDate(updatedDate);

        entityInstance.setInvoiceTax(10);

        System.out.println(entityInstance.getInvoiceTax());

        entityInstance.setInvoiceBalance(entityInstance.getInvoiceTotalAmount() - entityInstance.getInvoiceAmountPaid());
        entityInstance.setInvoiceTotalAmount(entityInstance.getClientSubscription().getSubscription().getSubscriptionPrice() + entityInstance.getInvoiceTax());

        Validate.notNull(entityInstance, "Invoice is not saved");
        sendInvoice(entityInstance);
        return save(entityInstance);
    }

    public void changeInvoiceNumber(Invoice entityInstance){
        int instanceCount = countInstances(search.addFilterEqual("recordStatus", RecordStatus.ACTIVE));

        if(instanceCount == 0){
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d" , 1 ));
        }
        else {
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d" , (instanceCount + 1 )));
        }
    }

    @Override
    public void deleteInstance(Invoice instance) throws OperationFailedException {

    }

    @Override
    public boolean isDeletable(Invoice instance) throws OperationFailedException {
        return false;
    }

    public void changeStatusToPendingApproval(Invoice instance){
        instance.setInvoiceStatus(InvoiceStatus.PENDING_APPROVAL);
        super.save(instance);
    }

    public void changeStatusToPaid(Invoice instance, double amount){
        instance.setInvoiceStatus(InvoiceStatus.PAID);
        instance.setInvoiceAmountPaid(amount);
        super.save(instance);

    }

    public void changeStatusToUnpaid(Invoice instance){
        instance.setInvoiceStatus(InvoiceStatus.UNPAID);
        super.save(instance);
    }

    public void changeStatusToPartiallyPaid(Invoice invoice, double amount) {
        invoice.setInvoiceStatus(InvoiceStatus.PARTIALLY_PAID);
        invoice.setInvoiceAmountPaid(amount);
        invoice.setInvoiceBalance(invoice.getInvoiceTotalAmount()-invoice.getInvoiceAmountPaid());
        super.save(invoice);
        sendInvoice(invoice);
    }



    @Override
    public List<Invoice> getInvoiceByStatus(){

        Search search = new Search().setDisjunction(true);

        search.addFilterEqual("invoiceStatus",InvoiceStatus.UNPAID);
        search.addFilterEqual("invoiceStatus",InvoiceStatus.PARTIALLY_PAID);

        search.addFilterEqual("createdBy", loggedInUser);

        return super.search(search);
    }

    public void sendInvoice(Invoice invoice){

        try {
            applicationEmailService.saveInvoice(invoice, "Invoice from Pahappa Ltd");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Invoice> getInvoiceByClientSubscriptionId(List<ClientSubscription> clientSubscriptions){
        List<Invoice> invoiceList = new ArrayList<>();

        for (ClientSubscription clientSubscription: clientSubscriptions){
            Search search = new Search();
            search.addFilterEqual("recordStatus",RecordStatus.ACTIVE);
            System.out.println("Number of client subscriptions  passed "+ clientSubscriptions.size());
            search.addFilterEqual("clientSubscription",clientSubscription);
            invoiceList.addAll(super.search(search));
        }

        System.out.println("The invoice List has "+ invoiceList.size());

        if(invoiceList==null){
            System.out.println("Null:"+null);
        }

        else{
            System.out.println("Not Null:"+ invoiceList.size());
        }
//        Invoice invoice = invoiceList.get(0);
//
//        System.out.println("Invoice Receipient:" + invoice.getClientSubscription().getClient().getClientEmail());
        return invoiceList;
    }

    public List<Invoice> getInvoiceByStatusPaid(Date startDate){
        Search search = new Search();
        search.addFilterEqual("invoiceStatus",InvoiceStatus.PAID);
        search.addFilterEqual("clientSubscription.subscriptionStartDate",startDate);
        return super.search(search);
    }

    @Override
    public List<Invoice> getInstances(Search search) {
        return null;
    }



}