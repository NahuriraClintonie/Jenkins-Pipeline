package org.pahappa.systems.core.constants;

public enum PaymentMethod {
   
    BANK("bank"),
    
    MTN_MOBILE_MONEY("mobilemoney"),
    AIRTEL_MONEY("airtelmoney"),

    CHEQUE("cheque"),
    CASH("cash");

    private String name;

    private PaymentMethod(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
}
