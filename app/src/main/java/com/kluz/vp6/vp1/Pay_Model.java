package com.kluz.vp6.vp1;

import java.io.Serializable;

/**
 * Created by kluz on 6/19/17.
 */

public class Pay_Model implements Serializable {

    private String cid;
    private  String name;
    private String amount;
    private  String date;
    private String image;

    public Pay_Model(String cid,String name,String amount,String date,String image){

        this.cid = cid;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.image = image;

    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
