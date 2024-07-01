package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.appEmail.EmailCarbonCopy;
import org.pahappa.systems.core.services.EmailCarbonCopyService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmailCarbonCopyServiceImpl extends GenericServiceImpl<EmailCarbonCopy> implements EmailCarbonCopyService {
    @Override
    public EmailCarbonCopy saveInstance(EmailCarbonCopy entityInstance) throws ValidationFailedException, OperationFailedException {
        return super.save(entityInstance);
    }

    @Override
    public boolean isDeletable(EmailCarbonCopy instance) throws OperationFailedException {
        return false;
    }

    @Override
    public List<EmailCarbonCopy> getCarbonCopiesByClientSubscriptionId(int clientSubscriptionId) {
        return null;
    }
}
