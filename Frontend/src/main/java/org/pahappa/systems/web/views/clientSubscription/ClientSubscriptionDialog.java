package org.pahappa.systems.web.views.clientSubscription;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.constants.SubscriptionTimeUnits;
import org.pahappa.systems.core.models.appEmail.EmailsToCc;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.*;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Getter
@Setter
@ManagedBean(name="clientSubscriptionDialog")
@SessionScoped
public class ClientSubscriptionDialog extends DialogForm<ClientSubscription>  {

    private ClientSubscriptionService clientSubscriptionService;
    private SubscriptionService subscriptionService;
    private ProductService productService;
    private UserService userService;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;
    private Client client;
    private List<Subscription> subscriptions;
    @Getter
    private List<Product> products;
    private List<Subscription> productSubscriptions;
    @Getter
    private List<InvoiceTax> invoiceTaxList;
    private List<InvoiceTax> selectedTaxList = new ArrayList<>();
    private InvoiceTaxService invoiceTaxService;
    private Subscription subscription;
    private Product selectedProduct;
    private List<SubscriptionTimeUnits> subscriptionTimeUnits;
    private List<User> userList;
    private List<String> selectedUserList = new ArrayList<>();
    private EmailsToCcService emailsToCcService;
    private boolean saveSuccessful;


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
    public void init() {
        this.clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        this.subscriptionService = ApplicationContextProvider.getBean(SubscriptionService.class);
        this.productService = ApplicationContextProvider.getBean(ProductService.class);
        this.invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.emailsToCcService = ApplicationContextProvider.getBean(EmailsToCcService.class);
        subscriptions = subscriptionService.getAllInstances();
        loadTaxes();
        loadProducts();
        loadUserList();
        subscriptionTimeUnits = Arrays.asList(SubscriptionTimeUnits.values());
        resetModal();

    }

    public void loadUserList(){
        try {
            userList = userService.getUsers();
        } catch (OperationFailedException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTaxes(){
        invoiceTaxList = invoiceTaxService.getAllInstances();
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
        CustomLogger.log("Client Subscription Dialog: Starting to save client subscription\n\n");
        model.setClient(client);
        model.setSubscriptionStatus(SubscriptionStatus.PENDING);
        startDate = model.getSubscriptionStartDate();
        selectedTimeUnit = model.getSubscription().getSubscriptionTimeUnits().toString();
        calculateEndDate(startDate, selectedTimeUnit);
        calculateDifferentReminderDates();

        try {
            ClientSubscription clientSubscription = this.clientSubscriptionService.saveInstance(super.model);
            saveSuccessful = true; // Set flag for successful save

            //Save the user email to be carbon copied
            for (String email: selectedUserList){
                EmailsToCc emailsToCc = new EmailsToCc();
                emailsToCc.setClientSubscriptionId(clientSubscription.getId());
                emailsToCc.setEmailAddress(email);
                emailsToCcService.saveInstance(emailsToCc);
            }

            CustomLogger.log("Client Subscription Dialog: Client subscription saved successfully\n\n");


            this.resetModal();
            super.hide();
        } catch (Exception e) {
            // Log error
            saveSuccessful = false;
            CustomLogger.log("Error saving client subscription");
        }
    }

    public void onDialogReturn() {
        if(saveSuccessful){
            MessageComposer.compose("Success", "Client Subscription saved successfully");
        }
        else {
            MessageComposer.warn("Error", "Failed to save client subscription ");
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

    public void calculateDifferentReminderDates(){
        LocalDate endDate = model.getSubscriptionEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //setting the dates for reminders before due date
        if(model.getSubscription().getNumberOfDaysBefore() != 0)
            model.setDateForDailyReminderBeforeDueDate(Date.from(endDate.minusDays(model.getSubscription().getNumberOfDaysBefore()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if(model.getSubscription().getNumberOfWeeksBefore() != 0)
            model.setDateForWeeklyReminderBeforeDueDate(Date.from(endDate.minusWeeks(model.getSubscription().getNumberOfWeeksBefore()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if(model.getSubscription().getNumberOfMonthsBefore() != 0)
            model.setDateForMonthlyReminderBeforeDueDate(Date.from(endDate.minusMonths(model.getSubscription().getNumberOfMonthsBefore()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        //setting the dates for reminders after due date
        if (model.getSubscription().getNumberOfDaysAfter() !=0)
            model.setDateForDailyReminderAfterDueDate(Date.from(endDate.plusDays(model.getSubscription().getNumberOfDaysAfter()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if (model.getSubscription().getNumberOfWeeksAfter() !=0)
            model.setDateForWeeklyReminderAfterDueDate(Date.from(endDate.plusWeeks(model.getSubscription().getNumberOfWeeksAfter()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if (model.getSubscription().getNumberOfMonthsAfter() !=0)
            model.setDateForMonthlyReminderAfterDueDate(Date.from(endDate.plusMonths(model.getSubscription().getNumberOfMonthsAfter()).atStartOfDay(ZoneId.systemDefault()).toInstant()));

    }

    public void resetModal(){
        super.resetModal();
        super.model = new ClientSubscription();
    }
    public boolean isSaveSuccessful() {
        return saveSuccessful;
    }

    public void setSaveSuccessful(boolean saveSuccessful) {
        this.saveSuccessful = saveSuccessful;
    }
}
