package com.quochao.demo.services.impl;

import com.quochao.demo.entities.Cart;
import com.quochao.demo.entities.CartItem;
import com.quochao.demo.exceptions.ResourceNotFoundException;
import com.quochao.demo.repositories.CartItemRepository;
import com.quochao.demo.repositories.CartRepository;
import com.quochao.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart checkout(Cart cart) {
        cart.getCartItems().forEach(c -> c.setCart(cart));
        cart.setQuantity(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum());
        cart.setTotalPrice(cart.getCartItems().stream().mapToDouble(CartItem::getTotalPrice).sum());
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found cart"));
    }

    @Override
    public Cart checkOrder(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found order has id " + id));
        cart.setState(true);
        return cart;
    }
}

