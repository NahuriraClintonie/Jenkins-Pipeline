package org.pahappa.systems.web.views.payment;

import org.pahappa.systems.core.models.payment.Payment;
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
public class PaymentAttachmentView extends DialogForm<Payment> {
    private Payment selectedPayment;
    private StreamedContent streamedContent;

    public PaymentAttachmentView() {
        super(HyperLinks.PAYMENT_ATTACHMENT_DIALOG, 400, 400);
    }

    @Override
    public void persist() throws Exception {

    }

    public void show(){
        PrimeFaces.current().executeScript("PF('paymentAttachmentViewDialog').show()");
    }

    public void setSelectedPayment(Payment payment){
        this.selectedPayment = payment;
        System.out.println("SelectedPayment has been set");
    }

    public StreamedContent getStreamedContent(){
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so
            // that it will generate right URL.
            System.out.println("Initial Phase");
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real
            // StreamedContent with the image bytes.
            //  this.pdfStream = generateFileContents();
            System.out.println("After Phase");
            buildDownloadableFile();
            System.out.println("The size in bytes "+ this.streamedContent.getContentLength());
            System.out.println("The content is" + this.streamedContent);
            return this.streamedContent;

        }
    }

    public void buildDownloadableFile(){
        InputStream inputStream = new ByteArrayInputStream(this.selectedPayment.getPaymentAttachment().getImageAttachment());
        this.streamedContent= new DefaultStreamedContent(inputStream, this.selectedPayment.getPaymentAttachment().getImageName());

    }
}
