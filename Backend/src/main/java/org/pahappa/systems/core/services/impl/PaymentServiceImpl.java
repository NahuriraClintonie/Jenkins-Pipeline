package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {

    @Override
    public Payment saveInstance(Payment entityInstance) throws ValidationFailedException, OperationFailedException {
        return null;
    }

    @Override
    public boolean isDeletable(Payment instance) throws OperationFailedException {
        return false;
    }
    
}
