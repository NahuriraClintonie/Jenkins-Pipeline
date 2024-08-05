package org.pahappa.systems.core.services;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface ProductService extends GenericService<Product> {
    List<Product> getProductList(Search search);
}
