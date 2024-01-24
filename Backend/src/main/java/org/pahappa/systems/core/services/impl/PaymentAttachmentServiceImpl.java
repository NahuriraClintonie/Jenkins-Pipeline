package org.pahappa.systems.core.services.impl;
//imports
import org.pahappa.systems.core.models.payment.PaymentAttachment;
import org.pahappa.systems.core.services.PaymentAttachmentService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentAttachmentServiceImpl extends GenericServiceImpl<PaymentAttachment> implements PaymentAttachmentService {
    @Override
    public PaymentAttachment saveInstance(PaymentAttachment entityInstance) throws ValidationFailedException, OperationFailedException {
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(PaymentAttachment instance) throws OperationFailedException {
        return false;
    }
}
