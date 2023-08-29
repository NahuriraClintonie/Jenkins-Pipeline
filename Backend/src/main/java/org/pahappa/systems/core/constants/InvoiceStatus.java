package org.pahappa.systems.core.constants;

/**
 * The state of the invoice at that time
 **/
public enum InvoiceStatus {

    /**
     * a PENDING_APPROVAL invoice is one that has benn generated but not yet approved
     *
     **/
    PENDING_APPROVAL,
    /**
     * a PENDING_PAYMENT invoice is one that is awaiting Payment after its approved
     * and amountPaid is still zero(0)
     **/
    PENDING_PAYMENT,
    /**
     * a paid invoice is one whose payment has been fully made to a zero(0) balance
     **/
    PAID,
    /**
     * an unpaid invoice is an invoice where no payment has been made
     * and the invoice is past its dueDate
     **/
    UNPAID,
    /**
     * partially_paid is one where an amount has been paid
     * but the invoiceBalance is not at zero
     * **/
    PARTIALLY_PAID


}
