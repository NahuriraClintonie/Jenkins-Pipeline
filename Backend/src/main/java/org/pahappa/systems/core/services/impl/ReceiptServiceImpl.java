package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.receipt.Receipt;
import org.pahappa.systems.core.services.ReceiptService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

public class ReceiptServiceImpl extends GenericServiceImpl<Receipt> implements ReceiptService{

    @Override
    public Receipt saveInstance(Receipt receipt) throws ValidationFailedException, OperationFailedException {
        return null;
    }

    @Override
    public boolean isDeletable(Receipt receipt) throws OperationFailedException {
        return false;
    }
    
}
