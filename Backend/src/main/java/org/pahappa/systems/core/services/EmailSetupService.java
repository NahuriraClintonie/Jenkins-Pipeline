package org.pahappa.systems.core.services;
//imports
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.services.base.GenericService;
<<<<<<< HEAD
=======
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
>>>>>>> 521cd72 (New updates)


public interface EmailSetupService extends GenericService<EmailSetup> {
    public EmailSetup getActiveEmail();
    // Search
    //recrd
    //setmax1
    // uniq
}
