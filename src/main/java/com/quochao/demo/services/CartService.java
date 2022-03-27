package com.quochao.demo.services;

import com.quochao.demo.entities.Cart;

public interface CartService {
    Cart createCart(Cart cart);

    Cart updateCart(Cart cartEntity);
}
