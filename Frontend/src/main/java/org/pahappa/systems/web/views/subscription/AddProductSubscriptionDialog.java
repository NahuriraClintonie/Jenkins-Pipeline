package org.pahappa.systems.web.views.subscription;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.SubscriptionTimeUnits;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@SessionScoped
@ManagedBean(name="addProductSubscriptionDialog")
public class AddProductSubscriptionDialog extends DialogForm<Subscription> {
    private SubscriptionService subscriptionService;
    private boolean subscriptionExists;
    private boolean showYearlyField;
    private SubscriptionTimeUnits subscriptionTimeUnit;
    private Product product;
    private List<SubscriptionTimeUnits> subscriptionTimeUnits;
    private boolean saveSuccessful;
    public void setProduct(Product product) {
        this.product = product;
        System.out.println(product.getProductName());

    }

    @PostConstruct
    public void init(){
        subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
        subscriptionTimeUnits = Arrays.asList(SubscriptionTimeUnits.values());
    }

    public AddProductSubscriptionDialog() {
        super(HyperLinks.ADD_PRODUCT_SUBSCRIPTION_DIALOG, 600, 490);
    }

    @Override
    public void persist() throws Exception {
        subscriptionExists = subscriptionService.getInstanceBySubscriptionProduct(product) != null;

        model.setProduct(product);
        this.subscriptionService.saveInstance(super.model);
        hide();
        this.resetModal();
        saveSuccessful = true;

    }

    public void displayFieldsDependingOnTimeUnits(SubscriptionTimeUnits newSubscriptionTimeUnit){
        if(newSubscriptionTimeUnit.equals(SubscriptionTimeUnits.YEARLY) || newSubscriptionTimeUnit.equals(SubscriptionTimeUnits.QUARTERLY)){
            showYearlyField = true;
            System.out.println("It has been set to true");
        }else{
            showYearlyField = false;
        }

    }

    public void resetModal(){
        super.resetModal();
        super.model = new Subscription();
    }

    public void onDialogReturn() {
        if(saveSuccessful){
            MessageComposer.compose("Success", "Added subscription to a product");
        }
        else {
            MessageComposer.compose("Error", "Failed to Set Product subscription");
        }
    }

}
