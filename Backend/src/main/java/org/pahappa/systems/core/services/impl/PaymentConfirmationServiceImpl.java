package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.payment.PaymentConfirmation;
import org.pahappa.systems.core.services.PaymentConfirmationService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

public class PaymentConfirmationServiceImpl extends GenericServiceImpl<PaymentConfirmation> implements PaymentConfirmationService {
    @Override
    public PaymentConfirmation saveInstance(PaymentConfirmation paymentConfirmation) throws ValidationFailedException, OperationFailedException {
        return null;
    }

    @Override
    public boolean isDeletable(PaymentConfirmation paymentConfirmation) throws OperationFailedException {
        return false;
    }
}
