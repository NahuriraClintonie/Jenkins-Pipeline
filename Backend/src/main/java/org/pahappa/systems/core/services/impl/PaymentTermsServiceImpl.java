package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.PaymentTermsService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PaymentTermsServiceImpl extends GenericServiceImpl<PaymentTerms> implements PaymentTermsService {

    @Override
    public PaymentTerms saveInstance(PaymentTerms paymentTerms) throws ValidationFailedException, OperationFailedException {

            Validate.notNull(paymentTerms, "Missing entity instance");
            return save(paymentTerms);
    }

    @Override
    public boolean isDeletable(PaymentTerms instance) throws OperationFailedException {
        return false;
    }

    @Override
    public PaymentTerms getById(String id) {
        return getInstanceByID(id);
    }

    @Override
    public PaymentTerms saveOrUpdate(PaymentTerms paymentTerms) {
        return entityManager.merge(paymentTerms);
    }
}
