package com.quochao.demo.services;

import com.quochao.demo.entities.CartItem;

import java.util.List;

public interface CartItemService {
    void addAllItems(List<CartItem> cartItems);
}
