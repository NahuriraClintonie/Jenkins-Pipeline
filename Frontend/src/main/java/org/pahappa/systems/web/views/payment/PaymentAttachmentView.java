package org.pahappa.systems.web.views.payment;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.models.payment.PaymentAttachment;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@ManagedBean(name="paymentAttachmentViewDialog")
@SessionScoped
@Setter
@Getter
public class PaymentAttachmentView extends DialogForm<Payment> {
    private Payment selectedPayment;
    private StreamedContent streamedContent;
    private boolean isPdf = false;

    public PaymentAttachmentView() {
        super(HyperLinks.PAYMENT_ATTACHMENT_DIALOG, 550, 550);
    }

    @Override
    public void persist() throws Exception {

    }

    public void show(){
        PrimeFaces.current().executeScript("PF('paymentAttachmentViewDialog').show()");
    }

    public void setSelectedPayment(Payment payment){
        this.selectedPayment = payment;
        this.streamedContent = buildDownloadableFile();
        System.out.println("SelectedPayment has been set");
    }

    public StreamedContent getStreamedContent(){
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            System.out.println("Initial Phase");
            return new DefaultStreamedContent();
        } else {
            System.out.println("After Phase");
            return this.streamedContent;

        }
    }

    public StreamedContent buildDownloadableFile() {
        PaymentAttachment paymentAttachment = this.selectedPayment.getPaymentAttachment();
        if (paymentAttachment != null) {
            if (paymentAttachment.getImageAttachment() != null && paymentAttachment.getName() != null) {
                isPdf = false;
                InputStream inputStream = new ByteArrayInputStream(paymentAttachment.getImageAttachment());
                return this.streamedContent = new DefaultStreamedContent(inputStream, "image/*", paymentAttachment.getName());
            } else if (paymentAttachment.getPdfAttachment() != null && paymentAttachment.getName() != null) {
                isPdf = true;
                InputStream inputStream = new ByteArrayInputStream(paymentAttachment.getPdfAttachment());
                return this.streamedContent = new DefaultStreamedContent(inputStream, "application/pdf", paymentAttachment.getName());
            } else {
                // Handle case when neither image nor PDF attachment is present
                this.streamedContent = null;
            }
        } else {
            // Handle case when payment attachment is null
            this.streamedContent = null;
        }
        return null;
    }
}
