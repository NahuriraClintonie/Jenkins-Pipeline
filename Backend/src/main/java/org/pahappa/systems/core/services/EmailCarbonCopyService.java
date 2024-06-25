package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.appEmail.EmailCarbonCopy;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface EmailCarbonCopyService extends GenericService<EmailCarbonCopy> {
    List<EmailCarbonCopy> getCarbonCopiesByClientSubscriptionId(int clientSubscriptionId);
}
