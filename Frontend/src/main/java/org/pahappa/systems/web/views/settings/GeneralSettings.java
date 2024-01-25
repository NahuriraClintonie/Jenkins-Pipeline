package org.pahappa.systems.web.views.settings;
//imports
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.EmailSetupService;
import org.pahappa.systems.core.services.InvoiceTaxService;

import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;


import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "generalSettingsView")
@SessionScoped
@Getter
@Setter
public class GeneralSettings extends WebFormView<EmailSetup, GeneralSettings, GeneralSettings> {
    private EmailSetupService emailSetupService;

    private InvoiceTaxService invoiceTaxService;
    private InvoiceTax invoiceTax;
    private ApplicationEmailService applicationEmailService;


    @Override
    public void persist() throws Exception {

    }

    public void save() throws ValidationFailedException, OperationFailedException {
        this.emailSetupService.saveInstance(super.model);
        UiUtils.showMessageBox("Action Successful", "EmailSetup is successful");
    }


    public void saveTaxToBeUsed() throws ValidationFailedException, OperationFailedException {
        this.invoiceTaxService.saveInstance(invoiceTax);
        UiUtils.showMessageBox("Action Successful", "Tax is successful");
    }

    @Override
    public void beanInit() {
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);

        if (invoiceTaxService.getAllInstances().isEmpty()) {
            invoiceTax = new InvoiceTax();
        } else {
            invoiceTax = invoiceTaxService.getAllInstances().get(0);
        }
    }

    @Override
    public void pageLoadInit() {

    }
}
