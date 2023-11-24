package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {

    @Override
    public Client saveInstance(Client entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(Client instance) throws OperationFailedException {
        return false;
    }


}
