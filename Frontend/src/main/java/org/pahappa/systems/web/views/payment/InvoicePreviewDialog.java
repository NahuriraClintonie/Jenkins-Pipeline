package org.pahappa.systems.web.views.payment;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;

@ManagedBean(name="invoicePreviewDialog")
@SessionScoped
public class InvoicePreviewDialog extends DialogForm<Invoice> {
    private StreamedContent pdfStream;

    private String invoiceNo;

    private Invoice invoice;
    public InvoicePreviewDialog() {
        super(HyperLinks.INVOICE_PREVIEW_DIALOG, 650, 550);
    }

    @Override
    public void persist() throws Exception {

    }

    public void setPdfStream(StreamedContent pdfStream) {
        this.pdfStream = pdfStream;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void show(){
        PrimeFaces.current().executeScript("PF('invoicePreviewDialog').show()");
    }

    public StreamedContent getPdfStream() {
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
            loadInvoice();
            return this.pdfStream;

        }
    }

    private void loadInvoice(){
        try {
           byte[] pdfContent = invoice.getInvoicePdf();
            System.out.println("Size of the array "+pdfContent.length);
            System.out.println(invoice.getInvoiceNumber());

            if (pdfContent != null) {

                String fileName = "Invoice_" + invoice.getInvoiceNumber() + ".pdf";
                System.out.println(fileName);
                pdfStream = new DefaultStreamedContent(new
                        ByteArrayInputStream(pdfContent), "application/pdf", fileName);


            } else {
                System.out.println("In the else");
                // Handle the case where PDF content is null
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "PDF content is null"));
            }
        } catch (Exception e) {
            // Handle the exception
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An error occurred: " + e.getMessage()));
        }
    }
}
