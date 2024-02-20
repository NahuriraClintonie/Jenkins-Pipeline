package org.pahappa.systems.core.services;
//imports
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.services.base.GenericService;


public interface EmailSetupService extends GenericService<EmailSetup> {
    EmailSetup getActiveEmail();
}
