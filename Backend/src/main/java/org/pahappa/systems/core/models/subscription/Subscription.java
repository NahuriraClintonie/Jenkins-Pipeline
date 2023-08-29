package org.pahappa.systems.core.models.subscription;

import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.constants.SubscriptionTimeUnits;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="subscriptions")

public class Subscription extends BaseEntity {
    private SubscriptionTimeUnits subscriptionTimeUnits;
    private String subscriptionDuration;

    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name="subscription_time_units")
    public SubscriptionTimeUnits getSubscriptionTimeUnits() {

        return subscriptionTimeUnits;
    }

    public void setSubscriptionTimeUnits(SubscriptionTimeUnits subscriptionTimeUnits) {
        this.subscriptionTimeUnits = subscriptionTimeUnits;
    }

    @Column(name="subscription_duration")
    public String getSubscriptionDuration() {
        return subscriptionDuration;
    }

    public void setSubscriptionDuration(String subscriptionDuration) {
        this.subscriptionDuration = subscriptionDuration;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return product.getProductName();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}