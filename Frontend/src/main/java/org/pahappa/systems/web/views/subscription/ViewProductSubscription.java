package org.pahappa.systems.web.views.subscription;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ViewPath(path = HyperLinks.VIEW_PRODUCT_SUBSCRIPTION)
@Getter
@Setter
@SessionScoped
@ManagedBean(name="viewProductSubscription")
public class ViewProductSubscription extends WebFormView<Subscription, ViewProductSubscription, SubscriptionView> {

    private SubscriptionService subscriptionService;
    private Subscription selectedSubscription;
    private Product selectedProduct;
    private List<Subscription> productSubscriptions;

    @Override
    public void persist() throws Exception {

    }

    @Override
    public void beanInit() {
        subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
    }

    @Override
    public void pageLoadInit() {

    }

    public void setSelectedProduct(Product selectedProduct) {
        if(selectedProduct != null && selectedProduct.getId() != null){
            this.selectedProduct = selectedProduct;
            productSubscriptions = subscriptionService.getInstanceBySubscriptionProduct(this.selectedProduct);

            for(Subscription subscription : productSubscriptions){
                if(subscription.getProduct().getId().equals(selectedProduct.getId())){
                    selectedSubscription = subscription;
                }
            }
        }else{
            System.out.println("Selected Product is null");
        }

    }
}






//public class ViewProductSubscription extends DialogForm<Subscription> {
//
//    private SubscriptionService subscriptionService;
//    private Subscription selectedSubscription;
//    private Product product;
//
//
//    @PostConstruct
//    public void init(){
//        subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
//    }
//
//    public void displaySelectedSubscription(Product product){
//        selectedSubscription = subscriptionService.getInstanceBySubscriptionProduct(product);
//    }
//
//    public ViewProductSubscription() {
//        super(HyperLinks.VIEW_PRODUCT_SUBSCRIPTION_DIALOG, 700, 370);
//    }
//
//    @Override
//    public void persist() throws Exception {
//        if(selectedSubscription != null) {
//            super.model= selectedSubscription;
//            subscriptionService.saveInstance(super.model);
//            resetModal();
//            hide();
//        }
//
//    }
//
//    public void resetModal(){
//        super.resetModal();
//        super.model = new Subscription();
//    }
//}

