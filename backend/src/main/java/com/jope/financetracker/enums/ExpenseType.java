package com.jope.financetracker.enums;

public enum ExpenseType {
    INCOME("INCOME"),
    EXPENSE("EXPENSE");


    private final String code;
    
    ExpenseType(String curr){
        this.code = curr;
    }

    public ExpenseType fromCode(String code){
        for(ExpenseType cur : ExpenseType.values()){
            if(cur.code.equals(code)){
                return cur;
            }
        }
        throw new IllegalArgumentException("Value not valid for ExpenseType!");
    }
}
