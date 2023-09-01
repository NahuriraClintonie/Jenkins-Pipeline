package org.pahappa.systems.core.constants;

public enum PaymentMethod {
   
    BANK("bank"),
    
    MOBILEMONEY("mobilemoney"),
    
    CASH("cash");

    private String name;

    private PaymentMethod(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
}
