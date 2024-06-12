package org.pahappa.systems.core.services;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.clientAccount.ClientAccount;
import org.pahappa.systems.core.services.base.GenericService;

public interface ClientAccountService extends GenericService<ClientAccount>{
    ClientAccount getParticularClientAccount(Search search);

    void updateClientAccount(ClientAccount clientAccount);

}
