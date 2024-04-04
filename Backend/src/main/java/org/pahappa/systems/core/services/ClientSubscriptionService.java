package org.pahappa.systems.core.services;
//imports
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.Date;
import java.util.List;


public interface ClientSubscriptionService extends GenericService <ClientSubscription> {
    public List<ClientSubscription> getClientSubscriptionsByEndDate(Date endDate);
    public List<ClientSubscription> getParticularClientSubscriptions(Client client);

    public List<ClientSubscription> getClientSubscriptionsThatAreNotInActive();

    void updateSubscriptionStatus(ClientSubscription clientSubscription);
}
