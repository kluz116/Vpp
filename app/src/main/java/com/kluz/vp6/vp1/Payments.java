package com.kluz.vp6.vp1;

import java.io.Serializable;

/**
 * Created by kluz on 3/28/17.
 */

public class Payments implements Serializable {
    private String amount;
    private String installemnts;
    private String daysPaid;
    private String valid_until;
    private String due_days;
    private  String outstanding;
    private  String pay_date;
    private String kit;

    public Payments() {
    }

    public Payments(String amount, String installemnts,String daysPaid,String valid_until,String due_days,String outstanding,String pay_date) {
        this.amount = amount;
        this.installemnts= installemnts;
        this.daysPaid = daysPaid;
        this.valid_until = valid_until;
        this.due_days = due_days;
        this.outstanding = outstanding;
        this.pay_date= pay_date;

    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInstallemnts() {
        return installemnts;
    }

    public void setInstallemnts(String installemnts) {
        this.installemnts = installemnts;
    }

    public String getDaysPaid() {
        return daysPaid;
    }
    public void setDaysPaid(String daysPaid) {
        this.daysPaid = daysPaid;
    }

    public String getValid_until() {
        return valid_until;
    }
    public void setValid_until(String valid_until) {
        this.valid_until = valid_until;
    }

    public String getDue_days() {
        return due_days;
    }
    public void setDue_days(String due_days) {
        this.due_days = due_days;
    }

    public String getOutstanding() {
        return outstanding;
    }
    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public String getPay_date() {
        return pay_date;
    }
    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

}
