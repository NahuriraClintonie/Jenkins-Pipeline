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
@ManagedBean(name="viewProductSubscriptionDialog")
public class ViewProductSubscriptionDialog extends DialogForm<Subscription> {

    private SubscriptionService subscriptionService;
    private Subscription selectedSubscription;
    private Product product;


    @PostConstruct
    public void init(){
        subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
    }

    public void displaySelectedSubscription(Product product){
        selectedSubscription = subscriptionService.getInstanceBySubscriptionProduct(product);
    }

    public ViewProductSubscriptionDialog() {
        super(HyperLinks.VIEW_PRODUCT_SUBSCRIPTION_DIALOG, 700, 370);
    }

    @Override
    public void persist() throws Exception {
        if(selectedSubscription != null) {
            super.model= selectedSubscription;
            subscriptionService.saveInstance(super.model);
            resetModal();
            hide();
        }

    }

    public void resetModal(){
        super.resetModal();
        super.model = new Subscription();
    }
}
