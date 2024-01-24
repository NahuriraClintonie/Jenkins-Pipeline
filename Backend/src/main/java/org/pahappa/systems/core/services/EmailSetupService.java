package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.services.base.GenericService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface EmailSetupService extends GenericService<EmailSetup> {
    public EmailSetup getActiveEmail();
    // Search
    //recrd
    //setmax1
    // uniq
}
