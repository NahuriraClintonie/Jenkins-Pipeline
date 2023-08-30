package org.pahappa.systems.core.models.receipt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.sers.webutils.model.BaseEntity;

import java.util.Date;

@Entity
@Table(name = "receipts")
public class Receipt extends BaseEntity{

    @Column(name = "receipt_date")
    private Date receiptDate;

    @Column(name = "received_from")
    private String receivedFrom;
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "amount_paid")
    private double amountPaid;

    @Column(name = "balance_due")
    private double balanceDue;

    @Column(name = "receipt_no")
    private String receiptNo;

    @Column(name = "received_by")
    private String receivedBy;

    @Column(name = "signature")
    private String signature;

    public Date getReceiptDate() {
        return new Date();
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceivedFrom() {
        return "Clinton";
    }

    public void setReceivedFrom(String receivedFrom ) {
        this.receivedFrom = receivedFrom ;
    }

    public String getPaymentMethod() {
        return "Bank";
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmountPaid() {
        return 100.0;
    }

    public void setAmountPaid(double amountPaid ) {
        this.amountPaid = amountPaid;
    }

    public double getBalanceDue() {
        return 0.0;
    }

    public void setBalanceDue(double balanceDue) {
        this.balanceDue = balanceDue;
    }

    public String getReceivedBy() {
        return "Kennedy";
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getSignature() {
        return "signature";
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReceiptNo() {
        return "receiptNo869";
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
}

