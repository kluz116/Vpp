package com.kluz.vp6.vp1;

/**
 * Created by kluz on 6/21/17.
 */

public class LogsModel {


    private  String date;
    private String responsibility;
    private String status;
    private String comment;

    public LogsModel( String date,String responsibility, String status,String comment){

        this.date = date;
        this.responsibility = responsibility;
        this.status = status;
        this.comment = comment;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
