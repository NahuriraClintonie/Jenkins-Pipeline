package org.pahappa.systems.core.models.invoice;

import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="invoices")
public class Invoice extends BaseEntity {
    private String invoiceNumber;
    private Date invoiceDueDate;
    private InvoiceStatus invoiceStatus;
    private double invoiceBalance;
    private double invoiceTotalAmount;
    private double invoiceAmountPaid;
    private double invoiceTax;
//    private Subscription subscription;
    private ClientSubscription clientSubscription;

    private String invoiceReference;

//    private byte[] invoicePdf; // BLOB data stored as a byte array

    @Column(name="invoice_number")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Column(name="invoice_due_date")
    public Date getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(Date invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    @Column(name="invoice_balance", columnDefinition = "double default 0.0")
    public double getInvoiceBalance() {
        return invoiceBalance;
    }

    public void setInvoiceBalance(double invoiceBalance) {
        this.invoiceBalance = invoiceBalance;
    }

    @Column(name="invoice_total_amount", columnDefinition = "double default 0.0")
    public double getInvoiceTotalAmount() {
        return invoiceTotalAmount;
    }

    public void setInvoiceTotalAmount(double invoiceTotalAmount) {
        this.invoiceTotalAmount = invoiceTotalAmount;
    }

    @Column(name="invoice_amount_paid", columnDefinition = "double default 0.0")
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

    @Enumerated(EnumType.STRING)
    @Column(name="invoice_status")
    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="subscription_id", referencedColumnName = "id")
//    public Subscription getSubscription(){
//        return subscription;
//    }
//
//    public void setSubscription(Subscription subscription){
//        this.subscription = subscription;
//    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_subscription_id", referencedColumnName = "id")
    public ClientSubscription getClientSubscription(){
       return clientSubscription;
   }
    public void setClientSubscription(ClientSubscription clientSubscription){
       this.clientSubscription = clientSubscription;
   }

   @Column(name="invoice_reference", nullable = false, columnDefinition = "default 'INITIAL'")
    public String getInvoiceReference() {
        return invoiceReference;
    }

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }


//    @Lob
//    @Column(name="invoice_pdf")
//    public byte[] getInvoicePdf() {
//        return invoicePdf;
//    }
//
//    public void setInvoicePdf(byte[] invoicePdf) {
//        this.invoicePdf = invoicePdf;
//    }
}
