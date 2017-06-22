package com.kluz.vp6.vp1;

import java.io.Serializable;

/**
 * Created by kluz on 2/24/17.
 */



public class clients implements Serializable {
    private String clients_name;
    private String phone;
    private String thumbnail;
    private String status;
    private  String current_out;
    private  String loan_out;
    private  String sales_p;
    private String kit;
    private  String valid_until;
    private String cid;


    public clients() {
    }

    public clients(String cid,String clients_name, String phone, String thumbnail,String status,String current_out,String loan_out,String sales_p,String kit,String valid_until) {
        this.clients_name = clients_name;
        this.phone= phone;
        this.thumbnail = thumbnail;
        this.status = status;
        this.current_out = current_out;
        this.loan_out = loan_out;
        this.sales_p = sales_p;
        this.kit= kit;
        this.valid_until=valid_until;
        this.cid=cid;
    }
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getName() {
        return clients_name;
    }
    public void setName(String clients_name) {
        this.clients_name = clients_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentOut() {
        return current_out;
    }
    public void setCurrentOut(String current_out) {
        this.current_out = current_out;
    }

    public String getLoan_outOut() {
        return loan_out;
    }
    public void setLoan_out(String loan_out) {
        this.loan_out = loan_out;
    }

    public String getSales_p() {
        return sales_p;
    }
    public void setSales_p(String sales_p) {
        this.sales_p = sales_p;
    }

    public String getKit() {
        return kit;
    }
    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getValid_until() {
        return valid_until;
    }
    public void setValid_until(String valid_until) {
        this.valid_until = valid_until;
    }

}

