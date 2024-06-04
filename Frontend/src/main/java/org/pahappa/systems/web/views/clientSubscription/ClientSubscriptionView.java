package org.pahappa.systems.web.views.clientSubscription;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.client.ClientView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ManagedBean(name = "clientSubscriptionView")
@SessionScoped
@ViewPath(path = HyperLinks.CLIENT_SUBSCRIPTION_VIEW)
public class ClientSubscriptionView extends WebFormView<ClientSubscription, ClientSubscriptionView, ClientView> {
    private ClientSubscriptionService clientSubscriptionService;

    private ApplicationEmailService applicationEmailService;

    private Search search;

    private List<SearchField> searchFields, selectedSearchFields;

    private String searchTerm;

    private Date createdFrom;

    private Date createdTo;

    private Client selectedClient;

    private List<ClientSubscription> clientSubscriptions;

    private Date startDate;


    @Override
    public void persist() throws Exception {

    }

    @Override
    public void beanInit() {
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        applicationEmailService = ApplicationContextProvider.getBean(ApplicationEmailService.class);
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

    public void activateOrDeactivateClientSubscription(ClientSubscription clientSubscription) throws ValidationFailedException, OperationFailedException {
        CustomLogger.log("ClientSubscriptionView: Activating or deactivating client subscription is starting");
        if(clientSubscription.getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE)){
            clientSubscription.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
            clientSubscriptionService.updateSubscriptionStatus(clientSubscription);

        }else {
            if(clientSubscription.getSubscriptionStatus().equals(SubscriptionStatus.INACTIVE)){
                Date currentDate = new Date();
                clientSubscription.setSubscriptionStartDate(currentDate);
                clientSubscription.setSubscriptionEndDate(calculateEndDate(currentDate, clientSubscription.getSubscription().getSubscriptionTimeUnits().toString()));
            }
            clientSubscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
            clientSubscriptionService.saveInstance(clientSubscription);
        }
        applicationEmailService.sendActivationOrDeactivationReminders(clientSubscription);
        CustomLogger.log("ClientSubscriptionView: Activating or deactivating client subscription is complete");
    }

    public Date calculateEndDate(Date startDate, String selectedTimeUnit) {
        if (startDate != null && selectedTimeUnit != null && !selectedTimeUnit.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            System.out.println("Selected time unit is "+ selectedTimeUnit);

            switch (selectedTimeUnit) {
                case "YEARLY":
                    calendar.add(Calendar.YEAR, 1);
                    break;
                case "QUARTERLY":
                    calendar.add(Calendar.MONTH, 3);
                    break;
                case "MONTHLY":
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case "WEEKLY":
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                default:
                    // Handle unexpected time units
                    break;
            }
            CustomLogger.log("ClientSubscriptionView: Calculating end date is complete");
            return calendar.getTime();
        }

        return null; // or throw an exception if needed
    }

    @Override
    public String getViewUrl() {
        return null;
    }
}