package org.pahappa.systems.core.services;

import org.pahappa.systems.core.services.base.GenericService;
import org.pahappa.systems.core.models.settings.ApplicationSettings;

/**
 * Responsible for CRUD operations on {@link ApplicationSettings}
 * @author ttc
 */
public interface ApplicationSettingsService extends GenericService<ApplicationSettings> {
    /**
     * Gets {@link ApplicationSettings}
     *
     * @return
     */
    ApplicationSettings getActiveApplicationSettings();
}
