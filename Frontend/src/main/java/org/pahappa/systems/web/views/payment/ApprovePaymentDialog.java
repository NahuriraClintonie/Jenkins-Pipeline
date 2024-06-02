package org.pahappa.systems.web.views.payment;
//imports
import lombok.Getter;
import lombok.Setter;

import org.pahappa.systems.core.constants.PaymentMethod;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.models.payment.PaymentAttachment;

import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
    private StreamedContent streamedContent;
    private InvoiceService invoiceService;
    private int state;
    public ApprovePaymentDialog() {
        super(HyperLinks.CONFIRM_PAYMENT_DIALOG, 800, 500);
    }

    @PostConstruct
    public void init(){
        super.model = new Payment();
        invoiceService= ApplicationContextProvider.getBean(InvoiceService.class);
        paymentService= ApplicationContextProvider.getBean(PaymentService.class);
        paymentMethods= Arrays.asList(PaymentMethod.values());
    }
    @Override
    public void persist() throws Exception {
        System.out.println("Added payment"+ model.getAmountPaid());
        model.setStatus(PaymentStatus.APPROVED);
        System.out.println("invoice status"+ model.getStatus());
        this.paymentService.saveInstance(model);
        hide();
    }

    public void rejectPayment() throws OperationFailedException, ValidationFailedException {
        this.model.setStatus(PaymentStatus.REJECTED);
        this.paymentService.saveInstance(this.model);
        this.invoiceService.changeStatusToUnpaid(this.model.getInvoice());
        hide();
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Payment();
    }

    public StreamedContent buildDownloadableFile(PaymentAttachment paymentAttachment){
        InputStream inputStream = new ByteArrayInputStream(paymentAttachment.getImageAttachment());
        return new DefaultStreamedContent(inputStream, paymentAttachment.getImageName());
    }

    @Override
    public void setModel(Payment model) {
        super.setModel(model);
        System.out.println("the attachment"+ super.model.getPaymentAttachment());
        streamedContent = buildDownloadableFile(super.model.getPaymentAttachment());
    }

}
