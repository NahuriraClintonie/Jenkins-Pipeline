package org.pahappa.systems.core.models.invoice;

import lombok.Getter;
import org.sers.webutils.model.BaseEntity;
import org.sers.webutils.model.security.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="invoicetax")
public class InvoiceTax extends BaseEntity {
    private String taxName;

    private double currentTax;

    private boolean taxedOnTotalAmount;

    public void setTaxedOnTotalAmount(boolean taxedOnTotalAmount) {
        this.taxedOnTotalAmount = taxedOnTotalAmount;
    }

    @Column(name="taxed_on_total_amount", columnDefinition = "boolean default false")
    public boolean getTaxedOnTotalAmount() {
        return taxedOnTotalAmount;
    }

    @Column(name="current_tax", columnDefinition = "double default 0.0")
    public double getCurrentTax() {
        return currentTax;
    }

    public void setCurrentTax(double currentTax) {
        this.currentTax = currentTax;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @Column(name="tax_name")
    public String getTaxName() {
        return taxName;
    }

    public boolean equals(Object other) {
        return other instanceof InvoiceTax && this.id != null ? this.id.equals(((InvoiceTax)other).id) : other == this;
    }

    public String toString() {
        if (this.taxName != null ) {
            return this.taxName + " - " + this.currentTax;
        } else {
            return "They are null";
        }
    }
}
