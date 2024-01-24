package org.pahappa.systems.web.views.paymentTerms;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.PaymentTermsService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.pahappa.systems.web.core.dialogs.MessageComposer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.util.Arrays;

@ManagedBean(name = "paymentTermsDialog")
@SessionScoped
@Getter
@Setter
public class PaymentTermsDialog extends DialogForm<PaymentTerms> {

    private PaymentTermsService paymentTermsService;
    private Search search;
    private boolean canAddTerms;
    private boolean canEditTerms;


    public PaymentTermsDialog() {
        super(HyperLinks.PAYMENT_TERMS_DIALOG, 800, 620);
    }

    @PostConstruct
    public void init(){
        this.paymentTermsService= ApplicationContextProvider.getBean(PaymentTermsService.class);
//        updateButtons();
    }

    @Override
    public void persist() throws Exception {
//        System.out.println("The Account No is: "+super.model.getAccountName());
        this.paymentTermsService.saveInstance(super.model);
        hide(); // Update buttons after saving instance
    }

    public void resetModal(){
        super.resetModal();
        super.model = new PaymentTerms();
        updateButtons(); // Update buttons when resetting modal
    }

    private void updateButtons() {
        canAddTerms = super.model == null;
        canEditTerms = super.model != null;
    }

}
