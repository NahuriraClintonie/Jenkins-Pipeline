package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.salesAgentReminder.SalesAgentReminder;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface SalesAgentReminderService extends GenericService<SalesAgentReminder> {
    List<SalesAgentReminder> getAllRemindersByDate();
}
