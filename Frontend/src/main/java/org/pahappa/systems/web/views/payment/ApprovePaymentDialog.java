package org.pahappa.systems.web.views.payment;

import lombok.Getter;
import lombok.Setter;

import org.pahappa.systems.core.constants.PaymentMethod;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="approvePaymentDialog")
@SessionScoped
@Setter
@Getter
public class ApprovePaymentDialog extends DialogForm<Payment> {

    private PaymentService paymentService;
    private Client currentClient;
    private Invoice invoice;
    private List<PaymentMethod> paymentMethods;

    public ApprovePaymentDialog() {
        super(HyperLinks.CONFIRM_PAYMENT_DIALOG, 800, 500);
    }

    private int state;

    @PostConstruct
    public void init(){
        super.model = new Payment();
        paymentService= ApplicationContextProvider.getBean(PaymentService.class);
        paymentMethods= Arrays.asList(PaymentMethod.values());
    }
    @Override
    public void persist() throws Exception {
        model.setStatus(PaymentStatus.APPROVED);
        this.paymentService.saveInstance(super.model);
        hide();
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Payment();
    }

}
