package org.pahappa.systems.web.views.subscription;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.web.views.UiUtils;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

@Getter
@Setter
@SessionScoped
@ManagedBean(name="addProductSubscriptionDialog")
public class AddProductSubscriptionDialog extends DialogForm<Subscription> {
    private SubscriptionService subscriptionService;
    private boolean subscriptionExists;

    public void setProduct(Product product) {
        this.product = product;
        System.out.println(product.getProductName());

    }

    private Product product;

    @PostConstruct
    public void init(){
        subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
    }

    public AddProductSubscriptionDialog() {
        super(HyperLinks.ADD_PRODUCT_SUBSCRIPTION_DIALOG, 700, 370);
    }

    @Override
    public void persist() throws Exception {
        subscriptionExists = subscriptionService.getInstanceBySubscriptionProduct(product) != null;

        if(subscriptionExists){
            UiUtils.showMessageBox("Subscription already exists", "Subscription already exists");
        }else{
            model.setProduct(product);
            this.subscriptionService.saveInstance(super.model);
            hide();
        }
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Subscription();
    }

}
