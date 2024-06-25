package org.pahappa.systems.web.views.payment;

import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ViewScoped
@ManagedBean(name="rejectedReasonDialog")
public class RejectedReasonDialog extends DialogForm<Payment> {

    public RejectedReasonDialog() {
        super(HyperLinks.REJECTED_PAYMENT_REASON_DIALOG, 200, 200);
    }

    @Override
    public void persist() throws Exception {

    }
}
