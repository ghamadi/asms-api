package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.LinkedHashMap;

@JsonPropertyOrder({"id", "name", "phone", "address", "projects"})
public class Client extends Entity {

    @JsonProperty
    private String id, name, phone, address;

    @JsonProperty
    private LinkedHashMap<String, Project> projects;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        markFieldAsSet("name");
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        markFieldAsSet("phone");
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        markFieldAsSet("address");
        this.address = address;
    }
}
