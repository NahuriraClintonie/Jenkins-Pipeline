package org.pahappa.systems.core.models.invoice;

import org.pahappa.systems.core.constants.InvoiceStatus;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="invoices")
public class Invoice extends BaseEntity {
    private String invoiceNumber;
    private Date invoiceGenerationDate;
    private Date invoiceDueDate;
    private InvoiceStatus invoiceStatus;
    private double invoiceBalance;
    private double invoiceTotalAmount;
    private double invoiceAmountPaid;
    private double invoiceTax;

    @Column(name="invoice_number")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Column(name="invoice_generation_date")
    public Date getInvoiceGenerationDate() {
        return invoiceGenerationDate;
    }

    public void setInvoiceGenerationDate(Date invoiceGenerationDate) {
        this.invoiceGenerationDate = invoiceGenerationDate;
    }

    @Column(name="invoice_due_date")
    public Date getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(Date invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    @Column(name="invoice_balance", columnDefinition = "integer 0.0")
    public double getInvoiceBalance() {
        return invoiceBalance;
    }

    public void setInvoiceBalance(double invoiceBalance) {
        this.invoiceBalance = invoiceBalance;
    }

    @Column(name="invoice_total_amount", columnDefinition = "integer 0.0")
    public double getInvoiceTotalAmount() {
        return invoiceTotalAmount;
    }

    public void setInvoiceTotalAmount(double invoiceTotalAmount) {
        this.invoiceTotalAmount = invoiceTotalAmount;
    }

    @Column(name="invoice_amount_paid", columnDefinition = "integer 0.0")
    public double getInvoiceAmountPaid() {
        return invoiceAmountPaid;
    }

    public void setInvoiceAmountPaid(double invoiceAmountPaid) {
        this.invoiceAmountPaid = invoiceAmountPaid;
    }

    @Column(name="invoice_tax")
    public double getInvoiceTax() {
        return invoiceTax;
    }

    public void setInvoiceTax(double invoiceTax) {
        this.invoiceTax = invoiceTax;
    }

    @Column(name="invoice_status")
    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
