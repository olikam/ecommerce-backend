package com.ecommerce.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class ProductUpdateRequest {

    @JsonIgnore
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
