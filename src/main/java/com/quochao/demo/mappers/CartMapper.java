package com.quochao.demo.mappers;

import com.quochao.demo.dtos.CartDTO;
import com.quochao.demo.entities.Cart;

public class CartMapper {
    private static CartMapper INSTANCE;

    public static CartMapper getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CartMapper();
        return INSTANCE;
    }

    public CartDTO toDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setName(cart.getUser().getName());
        dto.setQuantity(cart.getQuantity());
        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }
}
