package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.InvoiceTaxService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InvoiceTaxServiceImpl extends GenericServiceImpl<InvoiceTax> implements InvoiceTaxService {
    @Override
    public InvoiceTax saveInstance(InvoiceTax entityInstance) throws ValidationFailedException, OperationFailedException{

        Validate.notNull(entityInstance, "currentTax is not null");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(InvoiceTax instance) throws OperationFailedException {
        return false;
    }

    public InvoiceTax getTaxInstance(){
        Search search= new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.setMaxResults(1);
        return super.searchUnique(search);
    }
}
