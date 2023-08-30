package org.pahappa.systems.web.views.invoice;

import lombok.Getter;
import lombok.Setter;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;

import org.pahappa.systems.core.models.invoice.Invoice;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import org.sers.webutils.model.utils.SearchField;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;


@Getter
@Setter
@ManagedBean(name="invoiceApproveDialog")
public class InvoiceApproveDialog extends DialogForm<Invoice> {
    private InvoiceService invoiceService;
    @PostConstruct
    public void init(){
        invoiceService= ApplicationContextProvider.getBean(InvoiceService.class);
    }

    public InvoiceApproveDialog() {
        super(HyperLinks.INVOICE_APPROVE_DIALOG, 600, 600);
    }

    @Override
    public void persist() throws Exception {
        this.invoiceService.saveInstance(super.model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Invoice();
    }

    public void approve(){
        this.invoiceService.changeStatusToPendingPayment(super.model);
    }

}
