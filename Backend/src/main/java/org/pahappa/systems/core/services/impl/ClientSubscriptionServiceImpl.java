package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ClientSubscriptionServiceImpl extends GenericServiceImpl<ClientSubscription> implements ClientSubscriptionService {

    private InvoiceService invoiceService;
    Invoice invoice = new Invoice();

    @PostConstruct
    public void init(){
        this.invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
    }
    @Override
    public ClientSubscription saveInstance(ClientSubscription entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");

        invoice.setClientSubscription(entityInstance);
        this.invoiceService.saveInstance(invoice);
        System.out.println("null");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(ClientSubscription instance) throws OperationFailedException {
        return true;
    }

    public List<ClientSubscription> getClientSubscriptionsByEndDate(Date endDate){
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        search.addFilterEqual("subscriptionEndDate",endDate);

       return super.search(search);
    }

    public ClientSubscription getClientSubscriptionByStartDate(Date startDate,String clientId,String subscriptionId){
        Search search = new Search();
        search.addFilterEqual("recordStatus",RecordStatus.ACTIVE);
        search.addFilterEqual("subscriptionStartDate",startDate);
        search.addFilterEqual("client.id",clientId);
        search.addFilterEqual("subscription.id",subscriptionId);
        search.addFilterEqual("subscriptionStatus",SubscriptionStatus.PENDING);
       List<ClientSubscription> clientSubscriptionList = super.search(search);
        if(!clientSubscriptionList.isEmpty()){
       return clientSubscriptionList.get(0);}

        else{
            return null;
        }
    }
}
