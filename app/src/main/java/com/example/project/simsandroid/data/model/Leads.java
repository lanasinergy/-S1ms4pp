package com.example.project.simsandroid.data.model;

import java.io.Serializable;

public class Leads implements Serializable {

    private String lead_id;
    private String opp_name;
    private String nik;
    private String id_customer;
    private String closing_dates;
    private String results;
    private String amounts;
    private String info;
    private String assesment;
    private String proposed;
    private String proof;
    private String project_budget;
    private String priority;
    private String project_size;

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

    public String getAmount() {
        return amounts;
    }

    public void setAmount(String amount) {
        this.amounts = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAssesment() {
        return assesment;
    }

    public void setAssesment(String assesment) {
        this.assesment = assesment;
    }

    public String getProject_size() {
        return project_size;
    }

    public void setProject_size(String project_size) {
        this.project_size = project_size;
    }

    public String getProposed() {
        return proposed;
    }

    public void setProposed(String proposed) {
        this.proposed = proposed;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProject_budget() {
        return project_budget;
    }

    public void setProject_budget(String project_budget) {
        this.project_budget = project_budget;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

}
