package org.pahappa.systems.web.views.settings;
//imports
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
<<<<<<< HEAD
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.EmailSetupService;
import org.pahappa.systems.core.services.InvoiceTaxService;

import org.pahappa.systems.core.services.impl.ApplicationEmailServiceImpl;
import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
=======
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.EmailSetupService;
import org.pahappa.systems.core.services.PaymentService;

import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.client.views.presenters.PaginatedTable;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
>>>>>>> 521cd72 (New updates)
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
<<<<<<< HEAD
=======
import java.util.List;
import java.util.Map;
>>>>>>> 521cd72 (New updates)

@ManagedBean(name = "generalSettingsView")
@SessionScoped
@Getter
@Setter
public class GeneralSettings extends WebFormView<EmailSetup, GeneralSettings, GeneralSettings> {
    private EmailSetupService emailSetupService;
<<<<<<< HEAD
    private InvoiceTaxService invoiceTaxService;
    private InvoiceTax invoiceTax;
    private ApplicationEmailService applicationEmailService;

=======
>>>>>>> 521cd72 (New updates)

    @Override
    public void persist() throws Exception {

    }

    public void save() throws ValidationFailedException, OperationFailedException {
<<<<<<< HEAD
        applicationEmailService =new ApplicationEmailServiceImpl();
=======
>>>>>>> 521cd72 (New updates)
        this.emailSetupService.saveInstance(super.model);
        UiUtils.showMessageBox("Action Successful", "EmailSetup is successful");
    }

<<<<<<< HEAD
    public void saveTaxToBeUsed() throws ValidationFailedException, OperationFailedException {
        this.invoiceTaxService.saveInstance(invoiceTax);
        UiUtils.showMessageBox("Action Successful", "Tax is successful");
    }

    @Override
    public void beanInit() {
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);

        if(invoiceTaxService.getAllInstances().isEmpty()) {
            invoiceTax = new InvoiceTax();
        }else {
            invoiceTax = invoiceTaxService.getAllInstances().get(0);
        }
=======
    @Override
    public void beanInit() {
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
>>>>>>> 521cd72 (New updates)

        if (emailSetupService.getActiveEmail()==null){
            resetModal();
        }else{
            super.model = emailSetupService.getActiveEmail();
        }

    }

    @Override
    public void pageLoadInit() {

    }

<<<<<<< HEAD
    public void logOut(){
        invalidateSession();
    }

=======
>>>>>>> 521cd72 (New updates)
    public void resetModal(){
        super.resetModal();
        super.model = new EmailSetup();
    }
}
