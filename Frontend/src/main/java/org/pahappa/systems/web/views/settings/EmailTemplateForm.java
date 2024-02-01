package org.pahappa.systems.web.views.settings;

import org.pahappa.systems.core.models.emailTemplate.EmailTemplate;
import org.pahappa.systems.core.services.EmailTemplateService;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.pahappa.systems.core.constants.TemplateType;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;


import javax.faces.bean.SessionScoped;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@javax.faces.bean.ManagedBean(name="emailTemplateForm")
@ViewPath(path = HyperLinks.EMAIL_TEMPLATE_FORM)
public class EmailTemplateForm extends WebFormView<EmailTemplate, EmailTemplateForm, EmailTemplateView> {
    private static final long serialVersionUID = 1L;
    private EmailTemplateService emailTemplateService;
    private List<TemplateType> templateTypes;

    @Override
    public void beanInit() {
        resetModal();
        this.emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);
        this.templateTypes = Arrays.asList(TemplateType.values());
    }
    @Override
    public void persist() throws Exception {
        this.emailTemplateService.saveInstance(super.model);
    }

    @Override
    public void pageLoadInit() {

    }
    public void resetModal() {
        super.resetModal();
        super.model = new EmailTemplate();
    }

    public void setTemplateTypes(List<TemplateType> templateTypes) {
        this.templateTypes = templateTypes;
    }

    public List<TemplateType> getTemplateTypes() {
        return templateTypes;
    }
}
