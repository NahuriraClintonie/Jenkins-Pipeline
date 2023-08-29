package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.Date;
import java.util.List;


public interface ClientSubscriptionService extends GenericService <ClientSubscription> {
    public List<ClientSubscription> getClientSubscriptionsByEndDate(Date endDate);
}
