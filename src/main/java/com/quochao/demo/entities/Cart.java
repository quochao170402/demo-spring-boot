package com.quochao.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Integer quantity;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "created_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    public Cart() {
    }

    public Cart(Long id, User user, Integer quantity, Double totalPrice, LocalDate createdDate) {
        this.id = id;
        this.user = user;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", createdDate=" + createdDate +
                '}';
    }
}
