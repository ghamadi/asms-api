package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.LinkedHashMap;

@JsonPropertyOrder({"id", "title", "status", "delivery_date", "client_id", "client", "total_due", "payments", "sections"})
public class Project extends Entity {

    @JsonProperty
    private String id;

    @JsonProperty
    private String title;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("delivery_date")
    private Date deliveryDate;

    @JsonProperty("init_date")
    private Date initDate;

    @JsonProperty
    private String status;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LinkedHashMap<String, Payment> payments;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LinkedHashMap<String, ProjectSection> sections;


    public Project() {
        payments = new LinkedHashMap<>();
        sections = new LinkedHashMap<>();
    }

    @JsonProperty
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getId() {
        return id;
    }


    public void setTitle(String title) {
        markFieldAsSet("title");
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        markFieldAsSet("clientId");
        this.clientId = clientId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        markFieldAsSet("deliveryDate");
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        markFieldAsSet("status");
        this.status = status;
    }

    public LinkedHashMap<String, Payment> getPayments() {
        return payments;
    }

    public void setPayments(LinkedHashMap<String, Payment> payments) {
        this.payments = payments;
    }

    public LinkedHashMap<String, ProjectSection> getSections() {
        return sections;
    }

    public void setSections(LinkedHashMap<String, ProjectSection> sections) {
        this.sections = sections;
    }

    public void addSection(ProjectSection section) {
        if(sections == null)
            sections = new LinkedHashMap<>();
        sections.put(section.getId(), section);
    }

    public boolean containsSection(ProjectSection section) {
        return sections != null && sections.containsKey(section.getId());
    }

    public void addPayment(Payment payment){
        if(payments == null)
            payments = new LinkedHashMap<>();
        payments.put(payment.getId(), payment);
    }

    public boolean hasPayment(Payment payment){
        return payments != null && payments.containsKey(payment.getId());
    }

    @JsonProperty("total_due")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double getTotalDue(){
        if(sections == null || sections.isEmpty()) return null;
        return sections.values().stream()
                                .map(ProjectSection::totalDuePerSection)
                                .reduce(Double::sum)
                                .get();
    }

    @JsonProperty("total_paid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double getTotalPaid() {
        if(payments == null || payments.isEmpty()) return  0.0;
        return payments.values().stream()
                .map(Payment::getAmount)
                .reduce(Double::sum)
                .get();
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        markFieldAsSet("initDate");
        this.initDate = initDate;
    }
}
