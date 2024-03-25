package org.pahappa.systems.core.constants;

/**
 * The state of the sending invoices to a particular client
 **/
public enum AutoSendStatus {

    /**
     * With status set to true, invoices of a particular
     * client will always be sent
     *
     **/
    TRUE("true"),

    /**
     * With status set to false, invoices of a particular client will not be sent
     **/
    FALSE("false");

    private final String value;
    private  AutoSendStatus(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }

}