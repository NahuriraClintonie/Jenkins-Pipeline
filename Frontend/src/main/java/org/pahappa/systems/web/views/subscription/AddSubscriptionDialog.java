package org.pahappa.systems.web.views.subscription;

import lombok.Getter;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.ProductService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name="addSubscriptionDialog")
public class AddSubscriptionDialog extends DialogForm<Subscription> {
    private SubscriptionService subscriptionService;


    public void setProduct(Product product) {
        this.product = product;
        System.out.println(product.getProductName());

    }


    @Getter
    private Product product;



    @PostConstruct
    public void init(){
        subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
    }

    public AddSubscriptionDialog() {
        super(HyperLinks.ADD_SUBSCRIPTION_DIALOG, 700, 300);
    }

    @Override
    public void persist() throws Exception {
        model.setProduct(product);
        this.subscriptionService.saveInstance(super.model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Subscription();
    }

}
