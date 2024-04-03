package org.pahappa.systems.web.views.payment;
//imports
import lombok.Getter;
import lombok.Setter;

import org.pahappa.systems.core.constants.PaymentMethod;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;

import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.InvoiceService;

import org.pahappa.systems.core.models.payment.PaymentAttachment;
import org.pahappa.systems.core.services.PaymentAttachmentService;


import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.PaymentTermsService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import org.primefaces.model.UploadedFile;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;


import java.io.*;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name="paymentDialog")
@SessionScoped
@Setter
@Getter
public class PaymentDialog extends DialogForm<Payment> implements Serializable {


    private PaymentService paymentService;
    private Client currentClient;
    private Invoice invoice;
    private List<PaymentMethod> paymentMethods;
    private boolean showPhoneNumber;
    private boolean showAccountNumber;
    private boolean showChequeNumber;

    private InvoiceService invoiceService;
    private Payment payment;
    private PaymentTermsService paymentTermsService;

    private PaymentAttachment paymentAttachment;
    private PaymentAttachmentService paymentAttachmentService;

    private StreamedContent pdfStream;
    private String invoiceNo;


    public PaymentDialog() {
        super(HyperLinks.PAYMENT_DIALOG, 800, 500);
    }

    @PostConstruct
    public void init(){
        super.model = new Payment();
        paymentService= ApplicationContextProvider.getBean(PaymentService.class);
        paymentMethods= Arrays.asList(PaymentMethod.values());

        paymentTermsService = ApplicationContextProvider.getBean(PaymentTermsService.class);
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        paymentAttachmentService = ApplicationContextProvider.getBean(PaymentAttachmentService.class);
        paymentAttachment = new PaymentAttachment();
    }
    @Override
    public void persist() throws Exception {
        model.setInvoice(invoice);
        model.setStatus(PaymentStatus.PENDING);
        this.paymentService.saveInstance(super.model);
        hide();
    }


    public void resetModal(){
        super.resetModal();
        super.model = new Payment();
    }

    public void handlePaymentMethodChange() {
        System.out.println("Method is called");
        System.out.println(model.getPaymentMethod());
        if (model.getPaymentMethod() == PaymentMethod.BANK) {
            showPhoneNumber=false;
            showAccountNumber=true;
            showChequeNumber = false;
        } else if (model.getPaymentMethod() == PaymentMethod.MTN_MOBILE_MONEY || model.getPaymentMethod() == PaymentMethod.AIRTEL_MONEY) {
            // Show account number field and hide transaction ID field
            showPhoneNumber=true;
            showAccountNumber=false;
            showChequeNumber = false;
        }
        else if (model.getPaymentMethod() == PaymentMethod.CHEQUE) {
            // Show account number field and hide transaction ID field
            showPhoneNumber=false;
            showAccountNumber=false;
            showChequeNumber = true;
        } else {
            // For other payment methods, hide both fields
            showPhoneNumber=false;
            showAccountNumber=false;
            showChequeNumber = false;
        }
    }

    public void openInvoice(Invoice invoice) {
        try {
            byte[] pdfContent = invoice.getInvoicePdf();

            if (pdfContent != null) {
                String fileName = "Invoice_" + invoice.getInvoiceNumber() + ".pdf";

                pdfStream = new DefaultStreamedContent(new ByteArrayInputStream(pdfContent), "application/pdf", fileName);

                // Set the invoice property
                this.invoice = invoice;

                // Show the dialog
                PrimeFaces.current().executeScript("PF('invoicePreviewDlg').show()");
            } else {
                // Handle the case where PDF content is null
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "PDF content is null"));
            }
        } catch (Exception e) {
            // Handle the exception
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An error occurred: " + e.getMessage()));
        }
    }

    public void handleFileUpload(FileUploadEvent event){
        System.out.println("Starting image upload");
        UploadedFile uploadedFile = event.getFile();

        if (isValidContentType(uploadedFile.getContentType())) {
            byte[] receiptImageBytes = uploadedFile.getContents();
            String fileName = uploadedFile.getFileName();
            System.out.println("file name "+fileName);

            paymentAttachment.setImageAttachment(receiptImageBytes);
            paymentAttachment.setImageName(fileName);

            System.out.println("payment attachment file name "+ paymentAttachment.getImageName());

            try {
                model.setPaymentAttachment(paymentAttachmentService.saveInstance(paymentAttachment));

                System.out.println("model is " + model.getPaymentAttachment());
            } catch (ValidationFailedException e) {
                throw new RuntimeException(e);
            } catch (OperationFailedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("File is uploaded");
        } else {
            System.out.println("File ain't of the required type");
        }


    }


    private boolean isValidContentType(String contentType) {
        // Implement your logic to validate content type, e.g., check if it's an image
        return contentType != null && contentType.startsWith("image/") && (contentType.endsWith("jpeg") || contentType.endsWith("jpg") || contentType.endsWith("png") || contentType.endsWith("gif"));
    }
}
