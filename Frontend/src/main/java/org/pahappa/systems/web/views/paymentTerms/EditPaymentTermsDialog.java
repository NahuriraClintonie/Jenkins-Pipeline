package org.pahappa.systems.web.views.paymentTerms;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.PaymentTermsService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="editPaymentTermsDialog")
@SessionScoped
@Setter
@Getter
public class EditPaymentTermsDialog extends DialogForm<PaymentTerms> {
    private PaymentTermsService paymentTermsService;
    public EditPaymentTermsDialog(){
        super(HyperLinks.Edit_PAYMENT_DIALOG, 800, 620);
    }

    @PostConstruct
    public void init(){
        this.paymentTermsService= ApplicationContextProvider.getBean(PaymentTermsService.class);
        // Fetch the single PaymentTerms instance from the database
    }

    @Override
    public void persist() throws Exception {
        System.out.println("The Account Name is: "+super.model.getAccountName());
        paymentTermsService.saveOrUpdate(super.model);
        hide();
    }

    @Override
    public void setModel(PaymentTerms model) {
        super.setModel(model);
        super.model = paymentTermsService.getAllInstances().stream().findFirst().orElse(new PaymentTerms());
    }

}
