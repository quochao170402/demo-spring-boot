package com.quochao.demo.dtos;

import java.io.Serializable;

public class CartDTO implements Serializable {
    private Long id;
    private String name;
    private Integer quantity;
    private Double totalPrice;

    public CartDTO() {
    }

    public CartDTO(Long id, String name, Integer quantity, Double totalPrice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
