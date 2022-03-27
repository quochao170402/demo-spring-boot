package com.quochao.demo.services.impl;

import com.quochao.demo.entities.CartItem;
import com.quochao.demo.repositories.CartItemRepository;
import com.quochao.demo.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void addAllItems(List<CartItem> cartItems) {
        cartItemRepository.saveAll(cartItems);
    }
}
