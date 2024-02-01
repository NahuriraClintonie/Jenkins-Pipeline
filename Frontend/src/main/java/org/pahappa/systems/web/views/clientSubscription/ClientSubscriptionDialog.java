package org.pahappa.systems.web.views.clientSubscription;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.constants.SubscriptionTimeUnits;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.ProductService;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.time.ZoneId;
import java.util.Arrays;
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
    private ProductService productService;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;
    private Client client;
    private List<Subscription> subscriptions;
    @Getter
    private List<Product> products;
    private List<Subscription> productSubscriptions;
    private Date dateOnly;
    private Subscription subscription;
    @Getter
    private Product selectedProduct;
    private List<SubscriptionTimeUnits> subscriptionTimeUnits;

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Getter
    private Date startDate;

    public void setSelectedTimeUnit(String selectedTimeUnit) {
        this.selectedTimeUnit = selectedTimeUnit;
    }

    @Getter
    private String selectedTimeUnit;

    public void setTest(Subscription test) {
        this.test = test;
        System.out.println("this is the test");
    }

    @Getter
    private Subscription test;

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        System.out.println("Selected Product "+ this.selectedProduct.getProductName());
    }

    @PostConstruct
    public void init(){
        this.clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        this.subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
        this.productService = ApplicationContextProvider.getBean(ProductService.class);
        subscriptions = subscriptionService.getAllInstances();
        loadProducts();
        subscriptionTimeUnits = Arrays.asList(SubscriptionTimeUnits.values());
        resetModal();

    }

    public void setDateOnly(Date dateOnly) {
        this.dateOnly = dateOnly;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void loadSubscriptions() {
        System.out.println("Product name "+ this.selectedProduct);
        this.productSubscriptions = subscriptionService.getInstanceBySubscriptionProduct(selectedProduct);
        System.out.println("load subscription");
    }

    public void loadProducts(){
        products = productService.getAllInstances();
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        model.setSubscription(this.subscription);
        System.out.println("Subscription model "+ model.getSubscription().getSubscriptionName());
        System.out.println("Subscription name "+ subscription.getSubscriptionName());
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setClient(Client client) {
        this.client = client;
        System.out.println("My Client"+ client.getClientFirstName());
    }

    public ClientSubscriptionDialog() {

        super(HyperLinks.CLIENT_SUBSCRIPTION_DIALOG, 550, 350);
    }


    @Override
    public void persist() throws Exception {
        model.setClient(client);
        System.out.println("My Client"+ client.getClientFirstName());
        model.setSubscriptionStatus(SubscriptionStatus.PENDING);
        startDate= model.getSubscriptionStartDate();
        selectedTimeUnit = model.getSubscription().getSubscriptionTimeUnits().toString();
        calculateEndDate(startDate, selectedTimeUnit);
        System.out.println("End Date "+ model.getSubscriptionEndDate());

        this.clientSubscriptionService.saveInstance(super.model);
        System.out.println("Client Subscription saved successfully");
        hide();
        this.resetModal();

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

    public void resetModal(){
        super.resetModal();
        super.model = new ClientSubscription();
    }
}
