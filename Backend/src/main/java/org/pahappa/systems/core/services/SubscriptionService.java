package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.base.GenericService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;

import java.util.List;

public interface SubscriptionService extends GenericService<Subscription> {

    public List<Subscription> getInstanceBySubscriptionProduct(Product product);

}
