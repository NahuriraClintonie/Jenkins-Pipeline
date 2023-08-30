package org.pahappa.systems.core.models.invoice;

import org.sers.webutils.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="invoicetax")
public class InvoiceTax extends BaseEntity {

    @Column(name="current_tax")
    public double getCurrentTax() {
        return currentTax;
    }

    public void setCurrentTax(double currentTax) {
        this.currentTax = currentTax;
    }

    private double currentTax;
}
