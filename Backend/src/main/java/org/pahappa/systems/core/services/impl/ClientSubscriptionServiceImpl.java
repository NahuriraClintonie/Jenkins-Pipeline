package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

public class ClientSubscriptionServiceImpl extends GenericServiceImpl<ClientSubscription> implements ClientSubscriptionService {
    @Override
    public ClientSubscription saveInstance(ClientSubscription entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(ClientSubscription instance) throws OperationFailedException {
        return true;
    }
}
