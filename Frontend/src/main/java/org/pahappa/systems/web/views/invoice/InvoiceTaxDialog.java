package org.pahappa.systems.web.views.invoice;
//imports
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.InvoiceTaxService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@Getter
@Setter
@ManagedBean(name="invoiceTaxDialog")
@ViewScoped
public class InvoiceTaxDialog extends DialogForm<InvoiceTax> {
    private InvoiceTaxService invoiceTaxService ;

    @PostConstruct
    public void init(){
        super.model = new InvoiceTax();
        this.invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);
    }
    public InvoiceTaxDialog() {
        super(HyperLinks.INVOICE_CHANGE_TAX_DIALOG, 400, 200);
    }

    @Override
    public void persist() throws Exception {
        this.invoiceTaxService.saveInstance(super.model);
        resetModal();
        hide();
    }
    public void resetModal(){
        super.resetModal();
        super.model = new InvoiceTax();
    }
}
