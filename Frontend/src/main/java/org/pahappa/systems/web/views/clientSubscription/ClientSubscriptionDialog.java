package org.pahappa.systems.web.views.clientSubscription;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ManagedBean(name="clientSubscriptionDialog")
public class ClientSubscriptionDialog extends DialogForm<ClientSubscription> {
    private ClientSubscriptionService clientSubscriptionService;
    private SubscriptionService subscriptionService;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;

    @Getter
    private List<Subscription> dataModels;

    public void setDataModels(List<Subscription> dataModels) {
        this.dataModels = dataModels;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Getter
    private Client client;



    @PostConstruct
    public void init(){
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);

    }

    public ClientSubscriptionDialog() {
        super(HyperLinks.ADD_SUBSCRIPTION_DIALOG, 700, 300);
    }

//    public void reloadFromDB(int offset, int limit, Map<String, Object> map) throws Exception {
//        this.searchFields = Arrays.asList(new SearchField("FirstName", "firstName"), new SearchField("LastName", "lastName"),new SearchField("Email", "clientEmail"), new SearchField("Phone", "clientContact"));
//        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);
//        super.setDataModels(subscriptionService.getInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm,null, createdFrom, createdTo), offset, limit));
//    }

//    public List<Product> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
//        return getDataModels();
//    }


    @Override
    public void persist() throws Exception {
        model.setClient(client);
        model.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
        this.clientSubscriptionService.saveInstance(super.model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new ClientSubscription();
    }
}
