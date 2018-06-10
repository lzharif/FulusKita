package com.lzharif.fuluskita.model;

import com.google.firebase.database.Exclude;
import com.lzharif.fuluskita.helper.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransaction {
    public String transactionId;
    public double value;
    public String username;
    public String userId;
    public String desc;
    public long date;
    public int category; // Income or Expense
    public int type; // Type of Income or Expense itself
    public HashMap<String, Object> timestamp;

    public MoneyTransaction() {}

    public MoneyTransaction(String transactionId, double value, String userId,
                            String username, String desc, long date, int category,
                            int type, HashMap<String, Object> timestamp) {
        this.transactionId = transactionId;
        this.value = value;
        this.userId = userId;
        this.username = username;
        this.desc = desc;
        this.date = date;
        this.category = category;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String convertLongToDate(long dateLong) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        String dateString = android.text.format.DateFormat.format("dd-MM-yyyy", calendar).toString();
        return dateString;
    }

    public long convertDateToLong(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        long dateLong = 0;
        try {
            Date date = sdf.parse(dateString);
            dateLong = date.getTime();
            return dateLong;
        } catch (ParseException e) {
            e.printStackTrace();
            return dateLong;
        }
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constant.TRANSACTION_ID, transactionId);
        result.put(Constant.VALUE, value);
        result.put(Constant.USER_ID, userId);
        result.put(Constant.USERNAME, username);
        result.put(Constant.DESC, desc);
        result.put(Constant.DATE, date);
        result.put(Constant.CATEGORY, category);
        result.put(Constant.TYPE, type);
        result.put(Constant.TIMESTAMP, timestamp);

        return result;
    }
}
