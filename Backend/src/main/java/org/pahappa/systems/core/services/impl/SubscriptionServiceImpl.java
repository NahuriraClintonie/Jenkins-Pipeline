package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubscriptionServiceImpl extends GenericServiceImpl<Subscription> implements SubscriptionService {

    @Override
    public Subscription saveInstance(Subscription entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(Subscription instance) throws OperationFailedException {
        return true;
    }

    public List<Subscription> getInstanceBySubscriptionProduct(Product product) {
        return searchByPropertyEqual("product", product);
    }



}