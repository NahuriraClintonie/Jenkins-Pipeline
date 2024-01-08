package org.pahappa.systems.web.views.invoice;

import lombok.Getter;
import lombok.Setter;

import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.web.views.HyperLinks;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@Getter
@Setter
@SessionScoped
@ManagedBean(name="invoiceDialog")
public class InvoiceDialog extends DialogForm<Invoice> {

    public void setClient(Client client) {
        this.client = client;
    }

    private ClientSubscription clientSubscription;

    @Getter
    private Client client;

    private InvoiceService invoiceService;
    @PostConstruct
    public void init(){
        invoiceService= ApplicationContextProvider.getBean(InvoiceService.class);
    }

    public InvoiceDialog() {
        super(HyperLinks.INVOICE_DIALOG, 600, 600);
    }

    @Override
    public void persist() throws Exception {
        this.invoiceService.saveInstance(super.model);
        resetModal();
        hide();
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Invoice();
    }


}
