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
    UNPAID("Unpaid"),

    /**
     * a paid invoice is one whose payment has been fully made to a zero(0) balance
     **/
    PAID("Paid"),

    /**
     * partially_paid is one where an amount has been paid
     * but the invoiceBalance is not at zero
     * **/
    PARTIALLY_PAID("Partially_Paid"),

    /**
     * a pending_approval invoice is one where a payment has been made for it
     * and the salesAgent has captured the payment information
     * but the accountant has not yet approved the payment
     * */
    PENDING_APPROVAL("Pending_Approval");

    private String value;
    private  InvoiceStatus(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }

}
