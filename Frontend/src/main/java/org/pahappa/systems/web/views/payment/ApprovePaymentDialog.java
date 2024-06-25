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
import org.sers.webutils.server.shared.CustomLogger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

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
    private boolean isPdf;

    public ApprovePaymentDialog() {
        super(HyperLinks.CONFIRM_PAYMENT_DIALOG, 800, 500);
    }

    private int state;

    @PostConstruct
    public void init(){
        super.model = new Payment();
        isPdf = false;
        invoiceService= ApplicationContextProvider.getBean(InvoiceService.class);
        paymentService= ApplicationContextProvider.getBean(PaymentService.class);
        paymentMethods= Arrays.asList(PaymentMethod.values());
    }
    @Override
    public void persist() throws Exception {
        System.out.println("Persisting payment");
        this.model.setStatus(PaymentStatus.APPROVED);
        this.paymentService.saveInstance(this.model);
        hide();
    }

    public void rejectPayment() throws OperationFailedException, ValidationFailedException {
        System.out.println("Rejecting payment" + this.model.getReason());
        this.model.setStatus(PaymentStatus.REJECTED);
        this.paymentService.saveInstance(this.model);

        if (this.model.getInvoice().getInvoiceStatus().equals("PARTIALLY_PAID")) {
            this.invoiceService.changeStatusToPartiallyPaid(this.model.getInvoice(), this.model.getAmountPaid());
        } else if (this.model.getInvoice().getInvoiceStatus().equals("UNPAID")) {
            this.invoiceService.changeStatusToUnpaid(this.model.getInvoice());
        } else{
            CustomLogger.log("Invoice status is not paid or partially paid");
        }
        hide();
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Payment();
    }

    public StreamedContent buildDownloadableFile(PaymentAttachment paymentAttachment) {
        if (paymentAttachment.getImageAttachment() != null && paymentAttachment.getName() != null) {
            isPdf=false;
            InputStream inputStream = new ByteArrayInputStream(paymentAttachment.getImageAttachment());
            return new DefaultStreamedContent(inputStream, "image/*", paymentAttachment.getName());
        } else if (paymentAttachment.getPdfAttachment() != null && paymentAttachment.getName() != null) {
            isPdf=true;
            InputStream inputStream = new ByteArrayInputStream(paymentAttachment.getPdfAttachment());
            return new DefaultStreamedContent(inputStream, "application/pdf", paymentAttachment.getName());
        } else {
            return null;
        }
    }

    @Override
    public void setModel(Payment model) {
        super.setModel(model);
        streamedContent = buildDownloadableFile(super.model.getPaymentAttachment());
    }

    public StreamedContent getStreamedContent() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so
            // that it will generate right URL.
            System.out.println("Initial Phase");
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the pdf. Return a real
            // StreamedContent with the pdf bytes.
            //  this.pdfStream = generateFileContents();
            System.out.println("After Phase");
            return this.streamedContent;

        }
    }

}
