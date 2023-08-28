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

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "amount_in_words")
    private String amountInWords;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "amount_in_figures")
    private double amountInFigures;

    @Column(name = "balance_due")
    private double balanceDue;

    @Column(name = "received_by")
    private String receivedBy;

    @Column(name = "signature")
    private String signature;

    //getters, setters
    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAmountInWords() {
        return amountInWords;
    }

    public void setAmountInWords(String amountInWords) {
        this.amountInWords = amountInWords;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmountInFigures() {
        return amountInFigures;
    }

    public void setAmountInFigures(double amountInFigures) {
        this.amountInFigures = amountInFigures;
    }

    public double getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(double balanceDue) {
        this.balanceDue = balanceDue;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

