package org.pahappa.systems.web.views.clientSubscription;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceTaxService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ManagedBean(name="clientSubscriptionEditDialog")
@SessionScoped
public class ClientSubscriptionEditDialog extends DialogForm<ClientSubscription> {

    private ClientSubscriptionService clientSubscriptionService;
    private List<InvoiceTax> invoiceTaxList;
    private InvoiceTaxService invoiceTaxService;
    private List<InvoiceTax> selectedInvoiceTaxList;

    public ClientSubscriptionEditDialog()  {
        super(HyperLinks.CLIENT_SUBSCRIPTION_EDIT_DIALOG, 550, 350);
    }
    @PostConstruct
    public void init(){
        this.clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        this.invoiceTaxService =ApplicationContextProvider.getBean(InvoiceTaxService.class);
        invoiceTaxList = invoiceTaxService.getAllInstances();
    }

    @Override
    public void persist() throws Exception {
        model.setInvoiceTaxList(selectedInvoiceTaxList);
        clientSubscriptionService.saveInstance(model);
        hide();
    }

    @Override
    public void setFormProperties(){
        super.setFormProperties();
        if(super.model != null){
            this.selectedInvoiceTaxList = new ArrayList<>(model.getInvoiceTaxList());
        }
    }
}
