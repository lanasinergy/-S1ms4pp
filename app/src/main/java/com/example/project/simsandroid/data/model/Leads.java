package com.example.project.simsandroid.data.model;

import java.io.Serializable;

public class Leads implements Serializable {
    private String lead_id;
    private String opp_name;
    private String nik;
    private String id_customer;
    private String closing_dates;
    private String results;
    private Integer amounts;
    private String info;

    public String getResult() {
        return results;
    }

    public void setResult(String result) {
        this.results = result;
    }

    public String getClosing_date() {
        return closing_dates;
    }

    public void setClosing_date(String closing_date) {
        this.closing_dates = closing_date;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getOpp_name() {
        return opp_name;
    }

    public void setOpp_name(String opp_name) {
        this.opp_name = opp_name;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public Integer getAmount() {
        return amounts;
    }

    public void setAmount(Integer amount) {
        this.amounts = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
