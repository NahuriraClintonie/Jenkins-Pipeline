package org.pahappa.systems.core.constants;

/**
 * The state of the invoice at that time
 **/
public enum InvoiceStatus {

    /**
     * an unpaid invoice is one that is awaiting Payment after its generated
     * and sent to the Client
     * and amountPaid is still zero(0)
     **/
    UNPAID,
    /**
     * a paid invoice is one whose payment has been fully made to a zero(0) balance
     **/
    PAID,

    /**
     * partially_paid is one where an amount has been paid
     * but the invoiceBalance is not at zero
     * **/
    PARTIALLY_PAID


}
