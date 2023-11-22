package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.InvoiceStatus;
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

    @PostConstruct
    public void init(){
        invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);
        applicationEmailService = ApplicationContextProvider.getBean(ApplicationEmailService.class);
    }


    @Override
    public Invoice saveInstance(Invoice entityInstance) throws ValidationFailedException, OperationFailedException {

        int instanceCount = countInstances(search.addFilterEqual("recordStatus", RecordStatus.ACTIVE));

        if(instanceCount == 0){
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d" , 1 ));
        }
        else {
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d" , (instanceCount + 1 )));
        }

        if(entityInstance.getInvoiceStatus() == null){
            entityInstance.setInvoiceStatus(InvoiceStatus.UNPAID);
        }

        invoiceTaxList = invoiceTaxService.getAllInstances();
        int invoiceTaxCount = invoiceTaxList.size();

        InvoiceTax lastInvoiceTax = invoiceTaxList.get(invoiceTaxCount-1);

        entityInstance.setInvoiceTax(lastInvoiceTax.getCurrentTax());

        System.out.println(entityInstance.getInvoiceTax());

        Calendar calendar = Calendar.getInstance(); //create a calendar instance and set it to the current date
        calendar.setTime(currentDate);

        // Add a period (e.g., add 1 day)
        int daysToAdd = 15;
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Get the updated date
        Date updatedDate = calendar.getTime();
        entityInstance.setInvoiceDueDate(updatedDate);

        //if(entityInstance.getInvoiceTotalAmount() == 0.0 && entityInstance.getInvoiceAmountPaid()==0.0) {
            entityInstance.setInvoiceBalance(entityInstance.getInvoiceTotalAmount() - entityInstance.getInvoiceAmountPaid());
        //}
        entityInstance.setInvoiceTotalAmount(entityInstance.getClientSubscription().getSubscriptionPrice()+entityInstance.getInvoiceTax());

        Validate.notNull(entityInstance, "Invoice is not saved");
        sendInvoice(entityInstance );
         return save(entityInstance);




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

    public void changeStatusToPartiallyPaid(Invoice invoice, double amount){
        invoice.setInvoiceStatus(InvoiceStatus.PARTIALLY_PAID);
        invoice.setInvoiceAmountPaid(amount);
        invoice.setInvoiceBalance(invoice.getInvoiceTotalAmount()-invoice.getInvoiceAmountPaid());
        super.save(invoice);
        sendInvoice(invoice);
//        Invoice partialInvoice = new Invoice();
//        partialInvoice.setClientSubscription(invoice.getClientSubscription());
//        partialInvoice.setInvoiceTotalAmount(invoice.getInvoiceTotalAmount() - invoice.getInvoiceAmountPaid());
//        super.save(partialInvoice);
    }

    @Override
    public List<Invoice> getInvoiceByStatus(){

        Search search = new Search().setDisjunction(true);

        search.addFilterEqual("invoiceStatus",InvoiceStatus.UNPAID);
        search.addFilterEqual("invoiceStatus",InvoiceStatus.PARTIALLY_PAID);

        search.addFilterEqual("createdBy", loggedInUser);

        return super.search(search);
    }

//    public List<Invoice> getInvoicesForSalesAgent() {
//        Search search = new Search();
//        search.addFilterEqual("createdBy", loggedInUser.getId());
//        List<Invoice> invoiceList = super.search(search);
//        return invoiceList;
//    }

    public void sendInvoice(Invoice invoice){

        try {
            String invoiceContent = InvoiceService.generateInvoice(invoice);

            applicationEmailService.saveInvoice(invoice, "Invoice from Pahappa Ltd");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Invoice getInvoiceByClientSubscriptionId(String id){
        Search search = new Search();
        search.addFilterEqual("recordStatus",RecordStatus.ACTIVE);
        search.addFilterEqual("clientSubscription.id",id);


        List<Invoice> invoiceList = super.search(search);
        if(invoiceList==null){
            System.out.println("Null:"+null);
        }

        else{
            System.out.println("Not Null:"+ invoiceList.size());
        }
        Invoice invoice = invoiceList.get(0);

        System.out.println("Invoice Receipient:" + invoice.getClientSubscription().getClient().getClientEmail());
        return invoice;
    }

    public List<Invoice> getInvoiceByStatusPaid(Date startDate){
        Search search = new Search();
        search.addFilterEqual("invoiceStatus",InvoiceStatus.PAID);
        search.addFilterEqual("clientSubscription.subscriptionStartDate",startDate);
        return super.search(search);
    }


}