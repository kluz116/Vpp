package com.kluz.vp6.vp1;

import java.io.Serializable;

/**
 * Created by kluz on 3/4/17.
 */

public class Sales_model implements Serializable {
    private String clients_name;
    private String phone;
    private String status;
    private String barcode;
    private String revenue;
    private  String date;
    private  String sales_p;
    private String kit;
    private String cid;
    private  String image;

    public Sales_model() {
    }

    public Sales_model(String cid, String clients_name, String phone, String status,String barcode,String revenue,String date,String sales_p,String kit,String image) {
        this.clients_name = clients_name;
        this.phone= phone;
        this.status = status;
        this.barcode = barcode;
        this.revenue = revenue;
        this.sales_p = sales_p;
        this.kit= kit;
        this.date = date;
        this.cid= cid;
        this.image = image;
    }
    public String getCid(){return cid;}

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


    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getbarcode() {
        return barcode;
    }
    public void setbarcode(String nok) {
        this.barcode = barcode;
    }

    public String getNokrevenue() {
        return revenue;
    }
    public void setNokrevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getdate() {
        return date;
    }
    public void setdate(String date) {
        this.date = date;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
