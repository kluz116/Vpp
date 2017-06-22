package com.kluz.vp6.vp1;

import java.io.Serializable;

/**
 * Created by kluz on 2/24/17.
 */



public class Warning implements Serializable {
    private String cid;
    private String clients_name;
    private String phone;
    private String thumbnail;
    private String status;
    private String nok;
    private String nok_phone;
    private String current_out;
    private String loan_out;
    private String sales_p;
    private String kit;
    private String amount_paid;
    private String deposit_required;
    private String installation;
    private String disbursed;
    private  String pay_plan;
    private String technician;
    private String loanPeriod;
    private String barcode;
    private String district;
    private String subcounty;
    private String parish;
    private  String  address_desc;
    private  String overdue_value;
    private String customer_assoc_name;

    public Warning() {
    }

    public Warning(String cid,String clients_name, String phone, String thumbnail,String status,String nok,String nok_phone,String current_out,String loan_out,String sales_p,String kit, String amount_paid,String deposit_required, String installation, String disbursed, String pay_plan, String technician, String loanPeriod, String barcode, String district, String subcounty, String parish, String address_desc, String overdue_value,String customer_assoc_name) {
        this.cid = cid;
        this.clients_name = clients_name;
        this.phone= phone;
        this.thumbnail = thumbnail;
        this.status = status;
        this.nok = nok;
        this.nok_phone = nok_phone;
        this.current_out = current_out;
        this.loan_out = loan_out;
        this.sales_p = sales_p;
        this.kit= kit;
        this.amount_paid= amount_paid;
        this.deposit_required=deposit_required;
        this.installation = installation;
        this.disbursed = disbursed;
        this.pay_plan=pay_plan;
        this.technician = technician;
        this.loanPeriod = loanPeriod;
        this.barcode=barcode;
        this.district = district;
        this.subcounty = subcounty;
        this.parish = parish;
        this.address_desc = address_desc;
        this.overdue_value = overdue_value;
        this.customer_assoc_name = customer_assoc_name;


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

    public String getNok() {
        return nok;
    }
    public void setNok(String nok) {
        this.nok = nok;
    }

    public String getNokPhohe() {
        return nok_phone;
    }
    public void setNokPhone(String nok_phone) {
        this.nok_phone = nok_phone;
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

    public String getAmount_paid() {
        return amount_paid;
    }
    public void setAmount_paid(String amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getDeposit_required() {
        return deposit_required;
    }
    public void setDeposit_required(String deposit_required) {
        this.deposit_required = deposit_required;
    }
    public String getInstallation() {
        return installation;
    }
    public void setInstallation(String installation) {
        this.installation = installation;
    }
    public String getDisbursed() {
        return disbursed;
    }
    public void setDisbursed(String disbursed) {
        this.disbursed = disbursed;
    }
    public String getPay_plan() {
        return pay_plan;
    }
    public void setPay_plan(String pay_plan) {
        this.pay_plan = pay_plan;
    }

    public String getTechnician() {
        return technician;
    }
    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getAddress_desc() {
        return address_desc;
    }

    public void setAddress_desc(String address_desc) {
        this.address_desc = address_desc;
    }

    public String getOverdue_value() {
        return overdue_value;
    }

    public void setOverdue_value(String overdue_value) {
        this.overdue_value = overdue_value;
    }

    public String getCustomer_assoc_name() {
        return customer_assoc_name;
    }

    public void setCustomer_assoc_name(String customer_assoc_name) {
        this.customer_assoc_name = customer_assoc_name;
    }
}

