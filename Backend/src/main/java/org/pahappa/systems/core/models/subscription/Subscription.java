package org.pahappa.systems.core.models.subscription;

import lombok.Getter;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.constants.SubscriptionTimeUnits;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="subscriptions")

public class Subscription extends BaseEntity {
    private SubscriptionTimeUnits subscriptionTimeUnits;

    private Product product;

    private String subscriptionName;

    private double subscriptionPrice;

    @Enumerated(EnumType.STRING)
    @Column(name="subscription_time_units")
    public SubscriptionTimeUnits getSubscriptionTimeUnits() {

        return subscriptionTimeUnits;
    }

    public void setSubscriptionTimeUnits(SubscriptionTimeUnits subscriptionTimeUnits) {
        this.subscriptionTimeUnits = subscriptionTimeUnits;
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
        return super.getId() != null ? Objects.hash(super.getId()) : super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Subscription && (super.getId() != null) ? super.getId().equals(((Subscription) o).getId()) : (o == this);
    }

    @Column(name="subscription_name")
    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    @Column(name="subscription_price")
    public double getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(double subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

}