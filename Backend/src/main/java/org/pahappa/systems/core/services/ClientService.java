package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.services.base.GenericService;

public interface ClientService extends GenericService<Client> {
    public void updateAutoSendStatus(Client client);
}
