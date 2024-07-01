package org.pahappa.systems.web.views.paymentTerms;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.PaymentTermsService;
import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "paymentTermsSettings")
@SessionScoped
@Getter
@Setter
public class PaymentTermsSettings extends WebFormView<PaymentTerms, PaymentTermsSettings, PaymentTermsSettings> {

    private PaymentTermsService paymentTermsService;


    @Override
    public void persist() throws Exception {

    }

    @Override
    public void beanInit() {
        this.paymentTermsService= ApplicationContextProvider.getBean(PaymentTermsService.class);
        super.model = paymentTermsService.getAllInstances().stream().findFirst().orElse(new PaymentTerms());

    }

    public void save() throws ValidationFailedException, OperationFailedException {
        this.paymentTermsService.saveInstance(super.model);
        UiUtils.showMessageBox("Action Successful", "Payment terms added successfully");
    }

    @Override
    public void pageLoadInit() {

    }
}
