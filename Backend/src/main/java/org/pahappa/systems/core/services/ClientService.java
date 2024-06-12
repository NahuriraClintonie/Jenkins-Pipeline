package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientAccount.ClientAccount;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

public interface ClientService extends GenericService<Client> {
    void updateAutoSendStatus(Client client);
    void createClientAccount(Client client) throws ValidationFailedException, OperationFailedException;
}
