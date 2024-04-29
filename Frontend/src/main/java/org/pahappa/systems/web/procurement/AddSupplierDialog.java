package org.pahappa.systems.web.procurement;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.procurement.Supplier;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="supplierDialog")
@SessionScoped
@Setter
@Getter
public class AddSupplierDialog extends DialogForm<Supplier> {


    public AddSupplierDialog() {
        super(HyperLinks.SUPPLIER_DIALOG, 700, 400);
    }

    @Override
    public void persist() throws Exception {

    }
    public void resetModal(){
        super.resetModal();
        super.model = new Supplier();
    }
}
