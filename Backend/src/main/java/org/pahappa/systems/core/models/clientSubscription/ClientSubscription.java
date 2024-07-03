package org.pahappa.systems.core.models.clientSubscription;

import org.pahappa.systems.core.constants.SubscriptionStatus;
import org.pahappa.systems.core.models.appEmail.EmailsToCc;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.sers.webutils.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="client_subscriptions")

public class ClientSubscription extends BaseEntity {
    private Date subscriptionStartDate;
    private Date subscriptionEndDate;

    private SubscriptionStatus subscriptionStatus;

    private Client client;

    private Subscription subscription;

    private Date DateForDailyReminderBeforeDueDate;

    private Date DateForWeeklyReminderBeforeDueDate;

    private Date DateForMonthlyReminderBeforeDueDate;

    private Date DateForDailyReminderAfterDueDate;

    private Date DateForWeeklyReminderAfterDueDate;

    private Date DateForMonthlyReminderAfterDueDate;

    private List<InvoiceTax> invoiceTaxList;

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
                ", client=" + client +
                ", subscription=" + subscription +
                '}';
    }

    @Column(name="date_for_daily_reminder_before_due_date")
    public Date getDateForDailyReminderBeforeDueDate() {
        return DateForDailyReminderBeforeDueDate;
    }

    public void setDateForDailyReminderBeforeDueDate(Date dateForDailyReminderBeforeDueDate) {
        DateForDailyReminderBeforeDueDate = dateForDailyReminderBeforeDueDate;
    }

    @Column(name="date_for_weekly_reminder_before_due_date")
    public Date getDateForWeeklyReminderBeforeDueDate() {
        return DateForWeeklyReminderBeforeDueDate;
    }

    public void setDateForWeeklyReminderBeforeDueDate(Date dateForWeeklyReminderBeforeDueDate) {
        DateForWeeklyReminderBeforeDueDate = dateForWeeklyReminderBeforeDueDate;
    }

    @Column(name="date_for_monthly_reminder_before_due_date")
    public Date getDateForMonthlyReminderBeforeDueDate() {
        return DateForMonthlyReminderBeforeDueDate;
    }

    public void setDateForMonthlyReminderBeforeDueDate(Date dateForMonthlyReminderBeforeDueDate) {
        DateForMonthlyReminderBeforeDueDate = dateForMonthlyReminderBeforeDueDate;
    }

    @Column(name="date_for_daily_reminder_after_due_date")
    public Date getDateForDailyReminderAfterDueDate() {
        return DateForDailyReminderAfterDueDate;
    }

    public void setDateForDailyReminderAfterDueDate(Date dateForDailyReminderAfterDueDate) {
        DateForDailyReminderAfterDueDate = dateForDailyReminderAfterDueDate;
    }

    @Column(name="date_for_weekly_reminder_after_due_date")
    public Date getDateForWeeklyReminderAfterDueDate() {
        return DateForWeeklyReminderAfterDueDate;
    }

    public void setDateForWeeklyReminderAfterDueDate(Date dateForWeeklyReminderAfterDueDate) {
        DateForWeeklyReminderAfterDueDate = dateForWeeklyReminderAfterDueDate;
    }

    @Column(name="date_for_monthly_reminder_after_due_date")
    public Date getDateForMonthlyReminderAfterDueDate() {
        return DateForMonthlyReminderAfterDueDate;
    }

    public void setDateForMonthlyReminderAfterDueDate(Date dateForMonthlyReminderAfterDueDate) {
        DateForMonthlyReminderAfterDueDate = dateForMonthlyReminderAfterDueDate;
    }

    public void setInvoiceTaxList(List<InvoiceTax> invoiceTaxList) {
        this.invoiceTaxList = invoiceTaxList;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "client_subscription_invoice_tax"
            , joinColumns = @JoinColumn(name = "client_subscription_id")
            , inverseJoinColumns = @JoinColumn(name = "invoice_tax_id")
    )
    public List<InvoiceTax> getInvoiceTaxList() {
        return invoiceTaxList;
    }

    public boolean equals(Object other) {
        return other instanceof ClientSubscription && this.id != null ? this.id.equals(((ClientSubscription )other).id) : other == this;
    }


}