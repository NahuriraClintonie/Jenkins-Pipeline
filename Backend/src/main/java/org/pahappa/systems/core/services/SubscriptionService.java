package org.pahappa.systems.core.services;
//imports

import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface SubscriptionService extends GenericService<Subscription> {
    List<Subscription> getInstanceBySubscriptionProduct(Product product);

}
