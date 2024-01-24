package org.pahappa.systems.web.views.settings;
//imports
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
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
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "generalSettingsView")
@SessionScoped
@Getter
@Setter
public class GeneralSettings extends WebFormView<EmailSetup, GeneralSettings, GeneralSettings> {
    private EmailSetupService emailSetupService;

    @Override
    public void persist() throws Exception {

    }

    public void save() throws ValidationFailedException, OperationFailedException {
        this.emailSetupService.saveInstance(super.model);
        UiUtils.showMessageBox("Action Successful", "EmailSetup is successful");
    }

    @Override
    public void beanInit() {
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);

        if (emailSetupService.getActiveEmail()==null){
            resetModal();
        }else{
            super.model = emailSetupService.getActiveEmail();
        }

    }

    @Override
    public void pageLoadInit() {

    }

    public void resetModal(){
        super.resetModal();
        super.model = new EmailSetup();
    }
}
