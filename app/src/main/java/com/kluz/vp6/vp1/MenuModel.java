package com.kluz.vp6.vp1;

import java.io.Serializable;

/**
 * Created by kluz on 4/12/17.
 */

public class MenuModel implements Serializable {

    String menu_data;
    String id;
    String vpc;
    String sales_person_name;
    String icon;


    public MenuModel(String menu_data, String id,String vpc,String sales_person_name, String icon){
        this.menu_data= menu_data;
        this.id=id;
        this.vpc=vpc;
        this.sales_person_name=sales_person_name;
        this.icon= icon;

    }

    public String getMenu_data(){
        return  menu_data;
    }

    public void setMenu_data(String menu_data) {
        this.menu_data = menu_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVpc() {
        return vpc;
    }

    public void setVpc(String vpc) {
        this.vpc = vpc;
    }

    public String getSales_person_name() {
        return sales_person_name;
    }

    public void setSales_person_name(String sales_person_name) {
        this.sales_person_name = sales_person_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
