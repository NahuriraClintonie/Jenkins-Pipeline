package org.pahappa.systems.core.services.impl;

import org.pahappa.systems.core.models.gender.Gender;
import org.pahappa.systems.core.services.GenderService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class GenderServiceImpl extends GenericServiceImpl<Gender> implements GenderService {
    @Override
    public Gender saveInstance(Gender entityInstance) throws ValidationFailedException, OperationFailedException {
        return saveInstance(entityInstance);
    }

    @Override
    public boolean isDeletable(Gender instance) throws OperationFailedException {
        return true;
    }

    public void saveGender(Gender gender){
        save(gender);
    }

    @Override
    public List<Gender> getAllInstances() {
        return super.getAllInstances();
    }
}
