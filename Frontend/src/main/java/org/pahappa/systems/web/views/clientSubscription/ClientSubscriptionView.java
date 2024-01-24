package org.pahappa.systems.web.views.clientSubscription;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.client.ClientView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ManagedBean(name = "clientSubscriptionView")
@SessionScoped
@ViewPath(path = HyperLinks.CLIENT_SUBSCRIPTION_VIEW)
public class ClientSubscriptionView extends WebFormView<ClientSubscription, ClientSubscriptionView, ClientView> {
    private ClientSubscriptionService clientSubscriptionService;

    private Search search;

    private List<SearchField> searchFields, selectedSearchFields;

    private String searchTerm;

    private Date createdFrom;

    private Date createdTo;

    private Client selectedClient;

    private List<ClientSubscription> clientSubscriptions;


    @Override
    public void persist() throws Exception {

    }

    @Override
    public void beanInit() {
       clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
    }


    @Override
    public void pageLoadInit() {

    }
    
    public void setSelectedClient(Client selectedClient){
        if (selectedClient != null && selectedClient.getId() != null) {
            System.out.println("Client is not null");
            clientSubscriptions = clientSubscriptionService.getParticularClientSubscriptions(selectedClient);
            for (ClientSubscription clientSubscription : clientSubscriptions) {
                System.out.println(clientSubscription.getSubscription().getProduct().getProductName());
            }
        }
        else {
            System.out.println("Client is null");
        }
    }

//    public void deleteClientSubscription(ClientSubscription clientSubscription) throws OperationFailedException {
//
//        clientSubscriptionService.deleteInstance(clientSubscription);
//
//    }

    public void deactivateClientSubscription(ClientSubscription clientSubscription) throws ValidationFailedException, OperationFailedException {
        clientSubscription.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
        clientSubscriptionService.saveInstance(clientSubscription);
    }


    @Override
    public String getViewUrl() {
        return null;
    }
}
