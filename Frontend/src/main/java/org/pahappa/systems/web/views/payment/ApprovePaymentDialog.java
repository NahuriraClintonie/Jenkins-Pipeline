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
import org.pahappa.systems.web.core.dialogs.MessageComposer;
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
    private int state;
    private boolean isPdf;
    private String saveSuccessful ="empty";
    public ApprovePaymentDialog() {
        super(HyperLinks.CONFIRM_PAYMENT_DIALOG, 800, 500);
    }

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
        try{
            System.out.println("Persisting payment");
            this.model.setStatus(PaymentStatus.APPROVED);
            saveSuccessful = "approved"; // Set flag for successful save
            super.hide();
            this.paymentService.updatePaymentStatus(this.model);
        }catch (Exception e){
            saveSuccessful = "approveFailed";
        }

    }

    public void rejectPayment() throws OperationFailedException, ValidationFailedException {
        try{
            System.out.println("Rejecting payment" + this.model.getReason());
            this.model.setStatus(PaymentStatus.REJECTED);
            this.paymentService.saveInstance(this.model);
            saveSuccessful = "rejected";
            if (this.model.getInvoice().getInvoiceStatus().equals("PARTIALLY_PAID")) {
                this.invoiceService.changeStatusToPartiallyPaid(this.model.getInvoice(), this.model.getAmountPaid());
            } else if (this.model.getInvoice().getInvoiceStatus().equals("UNPAID")) {
                this.invoiceService.changeStatusToUnpaid(this.model.getInvoice());
            } else{
                CustomLogger.log("Invoice status is not paid or partially paid");
            }
            super.hide();
        } catch (Exception e){
            saveSuccessful = "rejectFailed";
        }
    }

    public void onDialogReturn() {
        if (!"empty".equals(saveSuccessful)){
            if("approved".equals(saveSuccessful)){
                MessageComposer.compose("Success", "Payment Approved successfully");
            }
            else if("rejected".equals(saveSuccessful)){
                MessageComposer.compose("Success", "Payment has been Rejected");
            }
            else if("approveFailed".equals(saveSuccessful)){
                MessageComposer.compose("Error", "Payment Approval Failed");
            }
            else if ("rejectFailed".equals(saveSuccessful)) {
                MessageComposer.compose("Error", "Payment Rejection Failed");
            }
            else{
                System.out.println("None has been executed");
            }
        }
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
        this.model = model;
        if (model.getPaymentAttachment() != null) {
            this.streamedContent = buildDownloadableFile(model.getPaymentAttachment());
        }
        else {
            this.streamedContent = null;
        }
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
