package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder({"id", "payment_date", "amount", "check_num", "project_id"})
public class Payment extends Entity {

    @JsonProperty
    private String id;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("payment_date")
    private Date paymentDate;

    @JsonProperty("check_num")
    private String checkNum;

    @JsonProperty
    private double amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        markFieldAsSet("projectId");
        this.projectId = projectId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        markFieldAsSet("paymentDate");
        this.paymentDate = paymentDate;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        markFieldAsSet("checkNum");
        this.checkNum = checkNum;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        markFieldAsSet("amount");
        this.amount = amount;
    }
}
