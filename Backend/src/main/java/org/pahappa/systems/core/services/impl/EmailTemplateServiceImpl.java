package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.TemplateType;
import org.pahappa.systems.core.models.emailTemplate.EmailTemplate;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.pahappa.systems.core.services.EmailTemplateService;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EmailTemplateServiceImpl extends GenericServiceImpl<EmailTemplate> implements EmailTemplateService{

    @Override
    public List<EmailTemplate> getTemplates() {
            Search search = new Search();
            search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
            return super.search(search);
        }

    @Override
    public EmailTemplate getEmailTemplateByType(TemplateType templateType) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if(templateType != null)
            search.addFilterEqual("templateType", templateType);
        search.setMaxResults(1);
        return super.searchUnique(search);
    }


    @Override
    public int countTemplates() {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.count(search);
    }

    @Override
    public EmailTemplate saveInstance(EmailTemplate emailTemplate) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(emailTemplate, "Missing template");
        Validate.notNull(emailTemplate.getTemplateType(), "Missing template type");
        Validate.notNull(emailTemplate.getTemplate(), "Missing template details");

        EmailTemplate existingTemplate = getEmailTemplateByType(emailTemplate.getTemplateType());
        if (existingTemplate != null && !Objects.equals(existingTemplate.getId(), emailTemplate.getId()))
            throw new ValidationFailedException(String.format("%s template already exists", emailTemplate.getTemplateType().getName()));

        return super.save(emailTemplate);
    }

    @Override
    public boolean isDeletable(EmailTemplate instance) throws OperationFailedException {
        return true;
    }
}
