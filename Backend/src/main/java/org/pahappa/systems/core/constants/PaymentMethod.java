package org.pahappa.systems.core.constants;

public enum PaymentMethod {
    /** The status when an accountant confirms the payment details that
     * were captured by the sales agent from the client **/
    CONFIRMED,
    /** The status when an accountant fails to confirm the 
     * payment details that were captured by the sales agent**/
    REJECTED,
    /** The status when the client's payment details are captured and stored
     * as they await to be confirmed by the account **/
    PENDING
    
}
