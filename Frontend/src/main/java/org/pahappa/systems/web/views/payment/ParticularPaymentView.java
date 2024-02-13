package org.pahappa.systems.web.views.payment;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="particularPaymentView")
@SessionScoped
@Getter
@Setter
@ViewPath(path = HyperLinks.PAYMENT_VIEW)
public class ParticularPaymentView extends WebFormView<Invoice, ParticularPaymentView, ParticularPaymentView> {
    private PaymentService paymentService;
    private List<Payment> particularInvoicePaymentList = new ArrayList<>();
    private Invoice selectedInvoice;

    @Override
    public void persist() throws Exception {

    }

    @Override
    public void beanInit() {
        paymentService = ApplicationContextProvider.getBean(PaymentService.class);
        System.out.println("The size is " +particularInvoicePaymentList.size());
    }

    @Override
    public void pageLoadInit() {

    }

    public void setSelectedInvoice(Invoice selectedInvoice){
        this.selectedInvoice = selectedInvoice;
        System.out.println("Invoice Number is "+selectedInvoice.getInvoiceNumber());
        particularInvoicePaymentList = paymentService.getAllPaymentsOfParticularInvoice(selectedInvoice.getId());
        System.out.println("List size is"+ particularInvoicePaymentList.size());

    }

    public void resetModal(){
        super.resetModal();
        super.model = new Invoice();
    }

}
