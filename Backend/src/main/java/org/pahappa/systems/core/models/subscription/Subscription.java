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

    private int numberOfDaysBefore;

    private int numberOfWeeksBefore;

    private int numberOfMonthsBefore;

    private int numberOfDaysAfter;

    private int numberOfWeeksAfter;

    private int numberOfMonthsAfter;

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

    @Column(name = "number_of_days_before", nullable = true)
    public int getNumberOfDaysBefore() {
        return numberOfDaysBefore;
    }

    public void setNumberOfDaysBefore(int numberOfDaysBefore) {
        this.numberOfDaysBefore = numberOfDaysBefore;
    }

    @Column(name = "number_of_weeks_before", nullable = true)
    public int getNumberOfWeeksBefore() {
        return numberOfWeeksBefore;
    }

    public void setNumberOfWeeksBefore(int numberOfWeeksBefore) {
        this.numberOfWeeksBefore = numberOfWeeksBefore;
    }

    @Column(name = "number_of_months_before", nullable = true)
    public int getNumberOfMonthsBefore() {
        return numberOfMonthsBefore;
    }

    public void setNumberOfMonthsBefore(int numberOfMonthsBefore) {
        this.numberOfMonthsBefore = numberOfMonthsBefore;
    }

    @Column(name = "number_of_days_after", nullable = true)
    public int getNumberOfDaysAfter() {
        return numberOfDaysAfter;
    }

    public void setNumberOfDaysAfter(int numberOfDaysAfter) {
        this.numberOfDaysAfter = numberOfDaysAfter;
    }

    @Column(name = "number_of_weeks_after", nullable = true)
    public int getNumberOfWeeksAfter() {
        return numberOfWeeksAfter;
    }

    public void setNumberOfWeeksAfter(int numberOfWeeksAfter) {
        this.numberOfWeeksAfter = numberOfWeeksAfter;
    }

    @Column(name = "number_of_months_after", nullable = true)
    public int getNumberOfMonthsAfter() {
        return numberOfMonthsAfter;
    }

    public void setNumberOfMonthsAfter(int numberOfMonthsAfter) {
        this.numberOfMonthsAfter = numberOfMonthsAfter;
    }

}