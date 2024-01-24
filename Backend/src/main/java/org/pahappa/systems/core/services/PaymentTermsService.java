package org.pahappa.systems.core.services;


import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.base.GenericService;

public interface PaymentTermsService extends GenericService<PaymentTerms> {
    PaymentTerms getById(String id);
    PaymentTerms saveOrUpdate(PaymentTerms paymentTerms);
}
