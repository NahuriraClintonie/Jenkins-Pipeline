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
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="paymentDialog")
@SessionScoped
@Setter
@Getter
public class PaymentDialog extends DialogForm<Payment> {

    private PaymentService paymentService;
    private Client currentClient;
    private Invoice invoice;
    private List<PaymentMethod> paymentMethods;
    private boolean showPhoneNumber;
    private boolean showAccountNumber;

    public PaymentDialog() {
        super(HyperLinks.PAYMENT_DIALOG, 800, 500);
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
        model.setInvoice(invoice);
        model.setStatus(PaymentStatus.PENDING);
        this.paymentService.saveInstance(super.model);
        hide();
    }

    public void show1(Client client){
        currentClient = client;
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Payment();
    }

    public void handlePaymentMethodChange() {
        System.out.println("Method is called");
        System.out.println(model.getPaymentMethod());
        if (model.getPaymentMethod() == PaymentMethod.BANK) {
            showPhoneNumber=true;
            showAccountNumber=false;
        } else if (model.getPaymentMethod() == PaymentMethod.MOBILEMONEY) {
            // Show account number field and hide transaction ID field
            showPhoneNumber=false;
            showAccountNumber=true;
        } else {
            // For other payment methods, hide both fields
            showPhoneNumber=false;
            showAccountNumber=false;
        }
        System.out.println("Phone: "+showPhoneNumber);
        System.out.println("Acc: " +showAccountNumber);
    }

}
