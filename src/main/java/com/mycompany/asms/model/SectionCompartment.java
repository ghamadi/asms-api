package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "project_section_id", "compartment_name", "product_id", "length", "width", "unit_price",  "product"})
public class SectionCompartment extends Entity {

    @JsonProperty
    private String id;

    @JsonProperty("project_section_id")
    private String projectSectionId;

    @JsonProperty("compartment_name")
    private String compartmentName;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty
    private double length;

    @JsonProperty
    private double width;

    @JsonProperty("unit_price")
    private double unitPrice;

    @JsonProperty
    private Product product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getProjectSectionId() {
        return projectSectionId;
    }

    public void setProjectSectionId(String projectSectionId) {
        markFieldAsSet("projectSectionId");
        this.projectSectionId = projectSectionId;
    }

    public String getCompartmentName() {
        return compartmentName;
    }

    public void setCompartmentName(String compartmentName) {
        markFieldAsSet("compartmentName");
        this.compartmentName = compartmentName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        markFieldAsSet("productId");
        this.productId = productId;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        markFieldAsSet("length");
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        markFieldAsSet("width");
        this.width = width;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        markFieldAsSet("unitPrice");
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
