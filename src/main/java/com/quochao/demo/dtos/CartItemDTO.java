package com.quochao.demo.dtos;

import java.io.Serializable;

public class CartItemDTO implements Serializable {
    private Long productId;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;

    public CartItemDTO() {
    }

    public CartItemDTO(Long productId, Double unitPrice, Integer quantity, Double totalPrice) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.totalPrice = this.quantity * this.unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
