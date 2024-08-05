package org.pahappa.systems.core.services;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientAccount.ClientAccount;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

import java.util.List;

public interface ClientService extends GenericService<Client> {
    void updateAutoSendStatus(Client client);
    void createClientAccount(Client client) throws ValidationFailedException, OperationFailedException;

    List<Client> returnAllRequiredInstances(Search search);
}
