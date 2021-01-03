package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product extends Entity {

    @JsonProperty
    private String id;

    @JsonProperty("product_type")
    private String productType;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("sales_unit")
    private String salesUnit;

    @JsonProperty("unit_price")
    private double unitPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        markFieldAsSet("productType");
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        markFieldAsSet("productName");
        this.productName = productName;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        markFieldAsSet("salesUnit");
        this.salesUnit = salesUnit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        markFieldAsSet("unitPrice");
        this.unitPrice = unitPrice;
    }
}
