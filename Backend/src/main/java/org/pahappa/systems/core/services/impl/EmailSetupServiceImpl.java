package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.services.EmailSetupService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailSetupServiceImpl extends GenericServiceImpl<EmailSetup> implements EmailSetupService {
    @Override
    public EmailSetup saveInstance(EmailSetup entityInstance) throws ValidationFailedException, OperationFailedException {
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(EmailSetup instance) throws OperationFailedException {
        return false;
    }


    @Override
    public EmailSetup getActiveEmail() {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.setMaxResults(1);

        return super.searchUnique(search);
    }
}
