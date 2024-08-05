package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.services.ProductService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl extends GenericServiceImpl<Product> implements ProductService {
    @Override
    public Product saveInstance(Product entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(Product instance) throws OperationFailedException {
        return true;
    }


    @Override
    public List<Product> getProductList(Search search) {
        search.addSortDesc("dateCreated");
        return super.search(search);
    }
}
