package com.jope.financetracker.enums;

public enum Currency {
    USD("USD"),
    BRL("BRL"),
    EUR("EUR");


    private final String code;
    
    Currency(String curr){
        this.code = curr;
    }

    public Currency fromCode(String code){
        for(Currency cur : Currency.values()){
            if(cur.code.equals(code)){
                return cur;
            }
        }
        throw new IllegalArgumentException("Value not valid for Currency!");
    }
}
