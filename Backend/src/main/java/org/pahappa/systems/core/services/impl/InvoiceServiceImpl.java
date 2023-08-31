package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.sendInvoice.SendInvoice;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Getter
@Setter
public class InvoiceServiceImpl extends GenericServiceImpl<Invoice> implements InvoiceService {
    Date currentDate = new Date();
    int instanceCount = 0;

    Search search = new Search();

    private List<Invoice> invoiceList;


    @Override
    public Invoice saveInstance(Invoice entityInstance) throws ValidationFailedException, OperationFailedException {
          invoiceList = getAllInstances();
          instanceCount = invoiceList.size();


        if(instanceCount == 0){
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d" , 1 ));
        }
        else {
            entityInstance.setInvoiceNumber(String.format("INVOICE-000%d" , (instanceCount + 1 )));
        }
        entityInstance.setInvoiceStatus(InvoiceStatus.PENDING_APPROVAL);

        Calendar calendar = Calendar.getInstance(); //create a calendar instance and set it to the current date
        calendar.setTime(currentDate);

        // Add a period (e.g., add 1 day)
        int daysToAdd = 4;
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Get the updated date
        Date updatedDate = calendar.getTime();
        entityInstance.setInvoiceDueDate(updatedDate);

        Validate.notNull(entityInstance, "Invoice is not saved");
        sendInvoice(entityInstance);

        return save(entityInstance);
    }

    @Override
    public void deleteInstance(Invoice instance) throws OperationFailedException {

    }

    @Override
    public boolean isDeletable(Invoice instance) throws OperationFailedException {
        return false;
    }

    public void changeStatusToPendingPayment(Invoice instance){
        instance.setInvoiceStatus(InvoiceStatus.PENDING_PAYMENT);
        save(instance);
    }

    public void sendInvoice(Invoice invoice){

        String invoiceContent = InvoiceService.generateInvoice(invoice);
        SendInvoice.sendInvoice(invoiceContent,invoice.getClientSubscription().getClient().getClientEmail());
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