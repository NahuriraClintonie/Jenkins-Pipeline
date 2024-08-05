package org.pahappa.systems.web.views.product;

import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.services.ProductService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="productDialog")
@SessionScoped
public class ProductDialog extends DialogForm<Product> {

    private ProductService productService;

    private Boolean saveSuccessful = null;

    @PostConstruct
    public void init(){
        productService = ApplicationContextProvider.getBean(ProductService.class);
    }

    public ProductDialog() {
        super(HyperLinks.PRODUCT_DIALOG, 700, 400);
    }

    @Override
    public void persist() throws Exception {
        try {
            this.productService.saveInstance(super.model);
            saveSuccessful = true;
            hide();
        } catch (Exception e){
            saveSuccessful = false;
            hide();
        }
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Product();
    }

    public void onDialogReturn() {
        if (saveSuccessful != null){
            if(saveSuccessful){
                MessageComposer.compose("Success", "Product saved successfully");
            }
            else {
                MessageComposer.compose("Error", "Failed to save Product");
            }
        }else {
            CustomLogger.log("Saved successfully is null");
        }
    }
}
