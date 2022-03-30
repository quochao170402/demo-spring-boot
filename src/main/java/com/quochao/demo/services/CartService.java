package com.quochao.demo.services;

import com.quochao.demo.entities.Cart;

import java.util.List;

public interface CartService {
    Cart checkout(Cart cart);

    List<Cart> findAll();

    Cart findById(Long id);

    Cart checkOrder(Long id);
}
