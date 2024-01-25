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
import java.time.LocalDate;
import java.time.ZoneId;
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

    private Search search;

    private List<SearchField> searchFields, selectedSearchFields;

    private String searchTerm;

    private Date createdFrom;

    private Date createdTo;

    private Client selectedClient;

    private String buttonLabel;

    private List<ClientSubscription> clientSubscriptions;

    private Date startDate;


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

<<<<<<< HEAD
    public void changeButton(ClientSubscription clientSubscription){
        if(clientSubscription.getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE)){
            buttonLabel = "Deactivate";
        }else{
            buttonLabel = "Activate";
        }
    }

    public void activateOrDeactivateClientSubscription(ClientSubscription clientSubscription) throws ValidationFailedException, OperationFailedException {
        if(clientSubscription.getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE)){
            clientSubscription.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
            clientSubscriptionService.saveInstance(clientSubscription);
        }else {
            if(clientSubscription.getSubscriptionStatus().equals(SubscriptionStatus.INACTIVE)){
                Date currentDate = new Date();
                clientSubscription.setSubscriptionStartDate(currentDate);
                clientSubscription.setSubscriptionEndDate(calculateEndDate(currentDate, clientSubscription.getSubscription().getSubscriptionTimeUnits().toString()));
            }
            clientSubscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
            clientSubscriptionService.saveInstance(clientSubscription);
        }
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
                // Add more cases for other time units if needed
                default:
                    // Handle unexpected time units
                    break;
            }

            // Adjust to the last day of the month

            System.out.println(calendar.getTime());
            model.setSubscriptionEndDate(calendar.getTime());
            return calendar.getTime();
        }

        return null; // or throw an exception if needed
    }
=======
    public void deactivateClientSubscription(ClientSubscription clientSubscription) throws ValidationFailedException, OperationFailedException {
        clientSubscription.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
        clientSubscriptionService.saveInstance(clientSubscription);
    }

>>>>>>> 4a8f0e3 (bg-fix-mergeIssues-two)

    @Override
    public String getViewUrl() {
        return null;
    }
}