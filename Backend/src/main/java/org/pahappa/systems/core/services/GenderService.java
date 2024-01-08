package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.gender.Gender;
import org.pahappa.systems.core.services.base.GenericService;

public interface GenderService extends GenericService<Gender> {

    public void saveGender(Gender gender);
}
