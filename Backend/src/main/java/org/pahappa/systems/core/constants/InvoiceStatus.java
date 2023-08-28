package org.pahappa.systems.core.constants;

public enum InvoiceStatus { /* The state of the invoice in question */
    /** a pending invoice is one that is awaiting Payment
     * and amountPaid is still zero(0)  **/
    PENDING,
    /** a paid invoice is one whose payment has been fully made to a zero(0) balance**/
    PAID,
    /** an unpaid invoice is an invoice where no payment has been made
     * and the invoice is past its dueDate**/
    UNPAID,
    /** partially_paid is one where an amount has been paid
     * but the invoiceBalance is not at zero**/
    PARTIALLY_PAID


}
