package com.kluz.vp6.vp1;

import java.io.Serializable;

/**
 * Created by kluz on 6/14/17.
 */

public class Statistics_Model implements Serializable {

    private String loan_month;
    private  String loan;


    public Statistics_Model( String loan,String loan_month){
        this.loan_month= loan_month;
        this.loan = loan;


    }

    public String getLoan_month() {
        return loan_month;
    }

    public void setLoan_month(String loan_month) {
        this.loan_month = loan_month;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }
}
