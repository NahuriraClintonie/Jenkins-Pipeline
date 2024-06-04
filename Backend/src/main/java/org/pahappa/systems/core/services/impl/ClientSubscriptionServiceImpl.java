package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ClientSubscriptionServiceImpl extends GenericServiceImpl<ClientSubscription> implements ClientSubscriptionService {

    private InvoiceService invoiceService;

    @PostConstruct
    public void init(){
        this.invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
    }
    @Override
    public ClientSubscription saveInstance(ClientSubscription entityInstance) throws ValidationFailedException, OperationFailedException {
        System.out.println("We are validating");
        Validate.notNull(entityInstance, "Missing entity instance");


        ClientSubscription savedClientSubscription = save(entityInstance);

        CustomLogger.log("Save Instance done in clientSubscriptionService");

        Invoice invoice = new Invoice();
        invoice.setClientSubscription(savedClientSubscription);
        this.invoiceService.saveInstance(invoice);

        return savedClientSubscription;
    }

    @Override
    public boolean isDeletable(ClientSubscription instance) throws OperationFailedException {
        return true;
    }

    public List<ClientSubscription> getClientSubscriptionsByEndDate(Date endDate){
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("SubscriptionStatus", SubscriptionStatus.ACTIVE);
        search.addFilterEqual("subscriptionEndDate",endDate);

        return super.search(search);
    }

    public List<ClientSubscription> getParticularClientSubscriptions(Client client){
        return searchByPropertyEqual("client", client);
    }

    @Override
    public List<ClientSubscription> getClientSubscriptionsThatAreNotInActive() {
        Search search = new Search();
        search.addFilterNotEqual("subscriptionStatus",SubscriptionStatus.ACTIVE);
        return super.search(search);
    }

    public List<ClientSubscription> getClientSubscriptionsThatArePending(){
        Search search = new Search();
        search.addFilterEqual("subscriptionStatus",SubscriptionStatus.PENDING);
        return super.search(search);
    }

    @Override
    public void updateSubscriptionStatus(ClientSubscription clientSubscription) {
        super.save(clientSubscription);
    }

    public void backgroundActivateClientSubscription() {
        List<ClientSubscription> clientSubscriptions = getClientSubscriptionsThatArePending();
        LocalDate currentDate = LocalDate.now(); // Current date

        if (clientSubscriptions != null && !clientSubscriptions.isEmpty()) {
            for (ClientSubscription clientSubscription : clientSubscriptions) {
                // Compare only the date part, ignoring the time
                if (isStartDateToday(clientSubscription.getSubscriptionStartDate(), currentDate)) {
                    clientSubscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                    super.save(clientSubscription);
                }
            }
        } else {
            CustomLogger.log("No client subscriptions to activate");
        }
    }

    // Method to check if the start date is today
    private boolean isStartDateToday(Date startDate, LocalDate currentDate) {
        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localStartDate.equals(currentDate);
    }
}