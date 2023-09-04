package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.sendInvoice.SendInvoice;
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
    int instanceCount = 0;

    Search search = new Search();

    private User loggedInUser = SharedAppData.getLoggedInUser();
    private InvoiceTaxService invoiceTaxService;

    private List<InvoiceTax> invoiceTaxList;

    @PostConstruct
    public void init(){
        invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);
    }


    @Override
    public Invoice saveInstance(Invoice entityInstance) throws ValidationFailedException, OperationFailedException {

        instanceCount = countInstances(search.addFilterEqual("recordStatus", RecordStatus.ACTIVE));

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
        int daysToAdd = 4;
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Get the updated date
        Date updatedDate = calendar.getTime();
        entityInstance.setInvoiceDueDate(updatedDate);

        if(entityInstance.getInvoiceTotalAmount() == 0.0 && entityInstance.getInvoiceAmountPaid()==0.0) {
            entityInstance.setInvoiceBalance(entityInstance.getInvoiceTotalAmount() - entityInstance.getInvoiceAmountPaid());
        }

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

    public void changeStatusToUnpaid(Invoice instance){
        instance.setInvoiceStatus(InvoiceStatus.UNPAID);
        super.persist(instance);
    }

    public void changeStatusToPaid(Invoice instance, double amount){
        instance.setInvoiceStatus(InvoiceStatus.PAID);
        instance.setInvoiceAmountPaid(amount);
        super.save(instance);
    }

    public void changeStatusToPartiallyPaid(Invoice invoice, double amount){
        invoice.setInvoiceStatus(InvoiceStatus.PARTIALLY_PAID);
        invoice.setInvoiceAmountPaid(amount);
        super.save(invoice);
    }

    @Override
    public List<Invoice> getInvoiceByStatus(){
//        System.out.println("Clintonie");
        Search search = new Search().setDisjunction(true);

        search.addFilterEqual("invoiceStatus",InvoiceStatus.UNPAID);
        search.addFilterEqual("invoiceStatus",InvoiceStatus.PARTIALLY_PAID);

        //search.addFilterEqual("createdBy", loggedInUser.getId());

        return super.search(search);
    }

//    public List<Invoice> getInvoicesForSalesAgent() {
//        Search search = new Search();
//        search.addFilterEqual("createdBy", loggedInUser.getId());
//        List<Invoice> invoiceList = super.search(search);
//        return invoiceList;
//    }

    public void sendInvoice(Invoice invoice){

        String invoiceContent = InvoiceService.generateInvoice(invoice);
        SendInvoice.sendInvoice(invoiceContent,invoice);
    }

    public Invoice getInvoiceByClientSubscriptionId(String id){
        Search search = new Search();
        search.addFilterEqual("recordStatus",RecordStatus.ACTIVE);
        search.addFilterEqual("clientSubscription.id",id);

        List<Invoice> invoiceList = super.search(search);
        Invoice invoice = invoiceList.get(0);
        System.out.println("Invoice Receipient:" + invoice.getClientSubscription().getClient().getClientEmail());
        return invoice;
    }

}