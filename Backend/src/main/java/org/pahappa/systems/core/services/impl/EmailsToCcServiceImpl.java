package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.appEmail.EmailsToCc;
import org.pahappa.systems.core.services.EmailSetupService;
import org.pahappa.systems.core.services.EmailsToCcService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmailsToCcServiceImpl extends GenericServiceImpl<EmailsToCc> implements EmailsToCcService {
    @Override
    public EmailsToCc saveInstance(EmailsToCc entityInstance) throws ValidationFailedException, OperationFailedException {
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(EmailsToCc instance) throws OperationFailedException {
        return false;
    }

    @Override
    public List<EmailsToCc> getByClientSubscritpion(String clientSubscriptionId) {
        Search search = new Search();
        search.addFilterEqual("clientSubscriptionId", clientSubscriptionId);
        return search(search);
    }
}
