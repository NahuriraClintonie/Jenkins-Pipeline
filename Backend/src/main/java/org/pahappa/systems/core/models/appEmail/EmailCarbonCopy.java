package org.pahappa.systems.core.models.appEmail;

import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.sers.webutils.model.BaseEntity;
import org.sers.webutils.model.security.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="carbon_copy_emails")
public class EmailCarbonCopy extends BaseEntity {
    private String UserId;
    private ClientSubscription clientSubscription;

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setClientSubscription(ClientSubscription clientSubscription) {
        this.clientSubscription = clientSubscription;
    }

    @OneToOne
    public ClientSubscription getClientSubscription() {
        return clientSubscription;
    }

}
