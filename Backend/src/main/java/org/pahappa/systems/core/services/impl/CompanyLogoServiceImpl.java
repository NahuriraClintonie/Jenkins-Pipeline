package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.companyLogo.CompanyLogo;
import org.pahappa.systems.core.services.CompanyLogoService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyLogoServiceImpl extends GenericServiceImpl<CompanyLogo> implements CompanyLogoService {
    @Override
    public CompanyLogo saveInstance(CompanyLogo entityInstance) throws ValidationFailedException, OperationFailedException {
        return super.save(entityInstance);
    }

    @Override
    public boolean isDeletable(CompanyLogo instance) throws OperationFailedException {
        return false;
    }
}
