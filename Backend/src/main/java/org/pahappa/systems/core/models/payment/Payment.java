package org.pahappa.systems.core.models.payment;

import org.pahappa.systems.core.constants.PaymentMethod;
import org.sers.webutils.model.BaseEntity;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="payments")
public class Payment extends BaseEntity {

    private Date paymentDate;
    private double amountPaid;
    private PaymentMethod paymentMethod;
    private String transactionID;
    private String phoneNumber;
    private String accountNumber;

    @Column(name="payment_date", nullable = false)
    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    @Column(name="amount_paid", nullable = false)
    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    @Column(name="payment_method", nullable = false)
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    @Column(name="transaction_id", nullable = false)
    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    @Column(name="phone_number", nullable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Column(name="account_number", nullable = true)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
