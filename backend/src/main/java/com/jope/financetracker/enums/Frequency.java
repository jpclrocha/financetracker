package com.jope.financetracker.enums;

public enum Frequency {
    DAILY("DAILY"),
    WEEKLY("WEEKLY"),
    MONTHLY("MONTHLY"),
    YEARLY("YEARLY");


    private final String code;
    
    Frequency(String curr){
        this.code = curr;
    }

    public Frequency fromCode(String code){
        for(Frequency cur : Frequency.values()){
            if(cur.code.equals(code)){
                return cur;
            }
        }
        throw new IllegalArgumentException("Value not valid for Frequency!");
    }
}
