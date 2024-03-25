package org.pahappa.systems.core.models.client;

import org.pahappa.systems.core.models.payment.Payment;
import org.sers.webutils.model.BaseEntity;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.security.User;

import javax.persistence.*;

@Entity
@Table(name="clients")
public class Client extends BaseEntity {
    private String clientFirstName;


    private String clientLastName;

    private String clientContact;

    private String clientEmail;

    private Gender clientGender;

    private User attachedTo;

    private boolean autoSendStatus;

    public Client() {
    }

    @Column(name="client_contact", nullable = false)
    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }
    @Column(name="client_email", nullable = false)
    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }


    @Column(name = "client_gender", nullable = false)
    public Gender getClientGender() {
        return clientGender;
    }

    public void setClientGender(Gender clientGender) {
        this.clientGender = clientGender;
    }

    @Column(name="client_first_name", nullable = false)
    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    @Column(name="client_last_name", nullable = false)
    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attachedTo", referencedColumnName = "id")
    public User getAttachedTo() {
        return attachedTo;
    }

    public void setAttachedTo(User attachedTo) {
        this.attachedTo = attachedTo;
    }

    @Column(name="auto_send_status", nullable = false)
    public boolean getAutoSendStatus() {
        return autoSendStatus;
    }

    public void setAutoSendStatus(boolean autoSendStatus) {
        this.autoSendStatus = autoSendStatus;
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
}