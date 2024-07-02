package org.pahappa.systems.core.models.appEmail;

import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "email_to_cc")
public class EmailsToCc extends BaseEntity {

    private String emailAddress;

    private String clientSubscriptionId;

    @Column(name = "email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Column(name = "client_subcription")
    public String getClientSubscriptionId() {
        return clientSubscriptionId;
    }

    public void setClientSubscriptionId(String clientSubscriptionId) {
        this.clientSubscriptionId = clientSubscriptionId;
    }
}
