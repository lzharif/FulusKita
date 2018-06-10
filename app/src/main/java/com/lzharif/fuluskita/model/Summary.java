package com.lzharif.fuluskita.model;

import com.google.firebase.database.Exclude;
import com.lzharif.fuluskita.helper.Constant;

import java.util.HashMap;
import java.util.Map;

public class Summary {
    public double currentBalance;
    public double currentDebit;
    public double currentCredit;

    public Summary() {
    }

    public Summary(double currentBalance, double currentDebit, double currentCredit) {
        this.currentBalance = currentBalance;
        this.currentDebit = currentDebit;
        this.currentCredit = currentCredit;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constant.CURRENT_BALANCE, currentBalance);
        result.put(Constant.CURRENT_CREDIT, currentCredit);
        result.put(Constant.CURRENT_DEBIT, currentDebit);
        return result;
    }


}
