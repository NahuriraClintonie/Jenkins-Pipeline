package org.pahappa.systems.core.models.appEmail;

import lombok.Getter;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.sers.webutils.model.BaseEntity;


import javax.mail.*;
import javax.persistence.*;

@Entity
@Table(name="app_emails")
public class AppEmail extends BaseEntity {
    private String emailSubject;

    private String emailMessage;

    private Invoice invoiceObject;

    private Payment paymentObject;

    private String senderEmail;

    private String senderPassword;

    private String receiverEmail;

    private byte[] emailAttachment;

    private Boolean emailStatus;

    //generate all the getters and setters for the above fields

    @Column(name="emailSubject", nullable = false)
    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    @Column(name="senderEmail", nullable = false)
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Column(name="receiverEmail", nullable = false)
    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    @Column(name="emailAttachment", nullable = true)
    public byte[] getEmailAttachment() {
        return emailAttachment;
    }

    public void setEmailAttachment(byte[] emailAttachment) {
        this.emailAttachment = emailAttachment;
    }

    @Column(name="emailStatus", nullable = false)
    public Boolean getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Boolean emailStatus) {
        this.emailStatus = emailStatus;
    }

    @Column(name="senderPassword", nullable = false)
    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    @Column(name="emailMessage", nullable = true, length = 1000)
    public String getEmailMessage() {
        return emailMessage;
    }

    @ManyToOne
    @JoinColumn(name = "invoice_object", nullable = true)
    public Invoice getInvoiceObject() {
        return invoiceObject;
    }

    public void setInvoiceObject(Invoice invoiceObject) {
        this.invoiceObject = invoiceObject;
    }

    @ManyToOne
    @JoinColumn(name = "payment_object" , nullable = true)
    public Payment getPaymentObject() {
        return paymentObject;
    }

    public void setPaymentObject(Payment paymentObject) {
        this.paymentObject = paymentObject;
    }

}
