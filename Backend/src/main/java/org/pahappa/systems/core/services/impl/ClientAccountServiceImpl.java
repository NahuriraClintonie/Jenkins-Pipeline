package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientAccount.ClientAccount;
import org.pahappa.systems.core.services.ClientAccountService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.shared.CustomLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@Service
@Transactional
public class ClientAccountServiceImpl extends GenericServiceImpl<ClientAccount> implements ClientAccountService {

    private Search search;

    @Override
    public ClientAccount saveInstance(ClientAccount entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance,"Client Account is null");
        CustomLogger.log("We are saving");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(ClientAccount instance) throws OperationFailedException {
        return false;
    }

    @Override
    public ClientAccount getParticularClientAccount(Search search) {
        this.search = search;
        return super.searchUnique(this.search);
    }

    @Override
    public void updateClientAccount(ClientAccount clientAccount) {
        super.update(clientAccount);
    }
}
