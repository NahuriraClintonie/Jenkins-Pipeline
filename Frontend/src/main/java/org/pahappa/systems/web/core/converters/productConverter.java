package org.pahappa.systems.web.core.converters;

import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.ProductService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.converters.base.GenericConverter;

import javax.faces.convert.FacesConverter;

@FacesConverter("productConverter")
public class productConverter extends GenericConverter<Product, ProductService> {
}
