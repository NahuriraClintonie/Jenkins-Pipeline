package org.pahappa.systems.core.models.clientAccount;

import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.payment.Payment;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "client_account")
public class ClientAccount extends BaseEntity {
    private double balance;
    private Client clientId;

    @Column(name = "balance", columnDefinition =  "double default 0.0")
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id", referencedColumnName = "id")
    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
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
