package org.pahappa.systems.web.views.clientSubscription;

import com.googlecode.genericdao.search.Search;
import lombok.CustomLog;
import lombok.Getter;
import org.pahappa.systems.core.Constants.SubscriptionStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.subscription.SubscriptionView;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name="clientSubscriptionDialog")
@SessionScoped
public class ClientSubscriptionDialog extends DialogForm<ClientSubscription>  {



    private ClientSubscriptionService clientSubscriptionService;
    private SubscriptionService subscriptionService;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;

    @Getter
    private Subscription subscription;

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Getter
    private List<Subscription> subscriptions;

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Getter
    private Client client;



    @PostConstruct
    public void init(){
        this.clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        this.subscriptions = ApplicationContextProvider.getBean(SubscriptionService.class).getAllInstances();


    }

    public ClientSubscriptionDialog() {
        super(HyperLinks.CLIENT_SUBSCRIPTION_DIALOG, 700, 300);
    }






    @Override
    public void persist() throws Exception {
        System.out.println("hey");
        model.setClient(client);
        model.setSubscription(subscription);
        model.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
        this.clientSubscriptionService.saveInstance(super.model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new ClientSubscription();
    }
}
