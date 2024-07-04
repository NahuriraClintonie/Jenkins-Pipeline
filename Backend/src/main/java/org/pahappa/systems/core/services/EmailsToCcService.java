package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.appEmail.EmailsToCc;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface EmailsToCcService extends GenericService<EmailsToCc> {
    List<EmailsToCc> getByClientSubscritpion(String clientSubscriptionId);
}
