package org.pahappa.systems.core.services;

import org.pahappa.systems.core.constants.TemplateType;
import org.pahappa.systems.core.models.emailTemplate.EmailTemplate;
import org.pahappa.systems.core.services.base.GenericService;

import java.util.List;

public interface EmailTemplateService extends GenericService<EmailTemplate> {
    /*
     * Retrieve all email templates
     */
    List<EmailTemplate> getTemplates();

    /*
     * Get template by type
     */
    EmailTemplate getEmailTemplateByType(TemplateType templateType);

    /*
     * Count templates
     */
    int countTemplates();
}
