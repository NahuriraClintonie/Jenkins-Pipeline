package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.invoice.Invoice;
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

@Service
@Transactional
public class InvoiceServiceImpl extends GenericServiceImpl<Invoice> implements InvoiceService {
    Date currentDate = new Date();
    int instanceCount = 0;

    Search search = new Search();


    @Override
    public Invoice saveInstance(Invoice entityInstance) throws ValidationFailedException, OperationFailedException {

        instanceCount = countInstances(search.addFilterEqual("recordStatus", RecordStatus.ACTIVE));

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
}