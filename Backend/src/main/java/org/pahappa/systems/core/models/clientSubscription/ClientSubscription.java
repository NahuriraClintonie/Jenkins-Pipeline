package org.pahappa.systems.core.models.clientSubscription;

import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="client_subscriptions")

public class ClientSubscription extends BaseEntity {
    private Date subscriptionStartDate;
    private Date subscriptionEndDate;

    private int numberOfDaysBeforeOverDueDate;
    private int numberOfDaysAfterOverDueDate;

    private SubscriptionStatus subscriptionStatus;

    private double subscriptionPrice;

     private Client client;

     private Subscription subscription;

     @Column(name="subscription_start_date")
    public Date getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(Date subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    @Column(name="subscription_end_date")
    public Date getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(Date subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    @Enumerated(EnumType.STRING)
    @Column(name="subscription_status")
    public SubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    @Column(name="subscription_price")
    public double getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(double subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "ClientSubscription{" +
                "subscriptionStartDate=" + subscriptionStartDate +
                ", subscriptionEndDate=" + subscriptionEndDate +
                ", subscriptionStatus=" + subscriptionStatus +
                ", subscriptionPrice=" + subscriptionPrice +
                ", client=" + client +
                ", subscription=" + subscription +
                '}';
    }

    public int getNumberOfDaysBeforeOverDueDate() {
        return numberOfDaysBeforeOverDueDate;
    }

    public void setNumberOfDaysBeforeOverDueDate(int numberOfDaysBeforeOverDueDate) {
        this.numberOfDaysBeforeOverDueDate = numberOfDaysBeforeOverDueDate;
    }

    public int getNumberOfDaysAfterOverDueDate() {
        return numberOfDaysAfterOverDueDate;
    }

    public void setNumberOfDaysAfterOverDueDate(int numberOfDaysAfterOverDueDate) {
        this.numberOfDaysAfterOverDueDate = numberOfDaysAfterOverDueDate;
    }
}
