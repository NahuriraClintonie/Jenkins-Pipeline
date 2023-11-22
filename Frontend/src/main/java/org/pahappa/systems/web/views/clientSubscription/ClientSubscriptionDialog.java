package org.pahappa.systems.web.views.clientSubscription;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.constants.SubscriptionTimeUnits;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ManagedBean(name="clientSubscriptionDialog")
@SessionScoped
public class ClientSubscriptionDialog extends DialogForm<ClientSubscription>  {



    private ClientSubscriptionService clientSubscriptionService;
    private SubscriptionService subscriptionService;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;


    private Date dateOnly;

    public void setDateOnly(Date dateOnly) {
        this.dateOnly = dateOnly;
    }


    private Subscription subscription;

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }


    private List<Subscription> subscriptions;

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    private Client client;



    @PostConstruct
    public void init(){
        this.clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        this.subscriptions = ApplicationContextProvider.getBean(SubscriptionService.class).getAllInstances();
        resetModal();

    }

    public ClientSubscriptionDialog() {
        super(HyperLinks.CLIENT_SUBSCRIPTION_DIALOG, 700, 430);
    }






    @Override
    public void persist() throws Exception {
        model.setClient(client);
        System.out.println("My Client"+ client.getClientFirstName());
        model.setSubscriptionStatus(SubscriptionStatus.PENDING);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateOnly());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        model.setSubscriptionStartDate(calendar.getTime());

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(model.getSubscriptionStartDate());
        if(model.getSubscription().getSubscriptionTimeUnits().equals(SubscriptionTimeUnits.YEARS)){
            calendar1.add(Calendar.YEAR,model.getSubscription().getSubscriptionDuration());
            model.setSubscriptionEndDate(calendar1.getTime());
        }
        else{
            calendar1.add(Calendar.MONTH,model.getSubscription().getSubscriptionDuration());
            model.setSubscriptionEndDate(calendar1.getTime());
        }

        System.out.println(model.getSubscriptionStartDate());
        System.out.println(model.getSubscription().getSubscriptionDuration());
        System.out.println(model.getSubscriptionEndDate());
        System.out.println("Name:"+ model.getSubscription().getProduct().getProductName());
        this.clientSubscriptionService.saveInstance(super.model);
        hide();
        this.resetModal();
    }

    public void resetModal(){
        super.resetModal();
        super.model = new ClientSubscription();
    }
}
