package com.quochao.demo.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class CartDTO implements Serializable {
    private Long id;
    private String name;
    private List<CartItemDTO> cartItemDTOs;
    private Integer quantity;
    private Double totalPrice;
    private LocalDate orderDate = LocalDate.now();

    public CartDTO() {
    }

    public CartDTO(Long id, String name, List<CartItemDTO> cartItemDTOs, Integer quantity, Double totalPrice, LocalDate orderDate) {
        this.id = id;
        this.name = name;
        this.cartItemDTOs = cartItemDTOs;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
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

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<CartItemDTO> getCartItemDTOs() {
        return cartItemDTOs;
    }

    public void setCartItemDTOs(List<CartItemDTO> cartItemDTOs) {
        this.cartItemDTOs = cartItemDTOs;
    }
}
