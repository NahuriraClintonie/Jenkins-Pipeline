package org.pahappa.systems.core.models.salesAgentReminder;

import org.pahappa.systems.core.models.invoice.Invoice;
import org.sers.webutils.model.BaseEntity;
import org.sers.webutils.model.security.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sales_agent_reminder")
public class SalesAgentReminder extends BaseEntity {

    private String message;

    private Invoice invoice;
    private User user;

    private boolean isRead;



    private Date sentDate;


    public void setMessage(String message) {
        this.message = message;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name="is_Read")
    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
       this.isRead = isRead;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Column(name="message")
    public String getMessage() {
        return message;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="invoice_id", referencedColumnName = "id", nullable= true)
    public Invoice getInvoice() {
        return invoice;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    @Column(name="sent_date")
    public Date getSentDate() {
        return sentDate;
    }
}
