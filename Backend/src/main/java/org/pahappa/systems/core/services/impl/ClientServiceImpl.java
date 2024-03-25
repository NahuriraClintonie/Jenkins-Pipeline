package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Client saveInstance(Client entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(Client instance) throws OperationFailedException {
        return true;
    }

    @Override
    public void updateAutoSendStatus(Client client) {
        // Merge the updated client entity into the persistence context
        System.out.println("Client auto status is: "+client.getAutoSendStatus());
        entityManager.merge(client);
    }
}
