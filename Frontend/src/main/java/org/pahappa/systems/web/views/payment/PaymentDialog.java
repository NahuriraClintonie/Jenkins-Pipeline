package org.pahappa.systems.web.views.payment;

import lombok.Getter;
import lombok.Setter;

import org.pahappa.systems.core.constants.PaymentMethod;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="paymentDialog")
@SessionScoped
@Setter
@Getter
public class PaymentDialog extends DialogForm<Payment> {

    private PaymentService paymentService;
    

    public PaymentDialog() {
        super(HyperLinks.PAYMENT_DIALOG, 700, 500);
    }

    @PostConstruct
    public void init(){
        paymentService= ApplicationContextProvider.getBean(PaymentService.class);
    }
    @Override
    public void persist() throws Exception {
        this.paymentService.saveInstance(super.model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Payment();
    }

}
