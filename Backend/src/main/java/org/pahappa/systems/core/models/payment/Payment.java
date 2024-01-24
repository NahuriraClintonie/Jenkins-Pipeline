package org.pahappa.systems.core.models.payment;

import org.pahappa.systems.core.constants.PaymentMethod;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.sers.webutils.model.BaseEntity;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="payments")
public class Payment extends BaseEntity {

    private Date paymentDate;

    private PaymentAttachment paymentAttachment;

    private double amountPaid;
    private PaymentMethod paymentMethod;
    private String transactionID;
    private String phoneNumber;
    private String accountNumber;
    private PaymentStatus status;

    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id") 
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Column(name="payment_date")
    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    @Column(name="amount_paid")
    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    @Column(name="payment_method")
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    @Column(name="transaction_id")
    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    @Column(name="phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Column(name="account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Column(name = "status")
    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Payment && (super.getId() != null)
                ?super.getId().equals(((Payment) obj).getId())
                :(obj == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null? this.getClass().hashCode() + super.getId().hashCode():super.hashCode();
    }

    @OneToOne
    @JoinColumn(name = "payment_attachment", referencedColumnName = "id")
    public PaymentAttachment getPaymentAttachment() {
        return paymentAttachment;
    }

    public void setPaymentAttachment(PaymentAttachment paymentAttachment) {
        this.paymentAttachment = paymentAttachment;
    }
}
