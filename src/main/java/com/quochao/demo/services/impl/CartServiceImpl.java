package com.quochao.demo.services.impl;

import com.quochao.demo.entities.Cart;
import com.quochao.demo.entities.CartItem;
import com.quochao.demo.repositories.CartRepository;
import com.quochao.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart createCart(Cart cart) {
        cart.setCreatedDate(LocalDate.now());
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart updateCart(Cart cartEntity) {
        Cart cart = cartRepository.getById(cartEntity.getId());
        cart = cartEntity;
        cart.setTotalPrice(cart.getCartItems().stream().mapToDouble(CartItem::getTotalPrice).sum());
        cart.setQuantity(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum());
        return cart;
    }
}
