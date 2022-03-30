package com.quochao.demo.mappers;

import com.quochao.demo.dtos.CartItemDTO;
import com.quochao.demo.entities.CartItem;

public class CartItemMapper {
    private static CartItemMapper INSTANCE;

    public static CartItemMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CartItemMapper();
        }
        return INSTANCE;
    }

//  ???
    public CartItem toEntity(CartItemDTO dto) {
       CartItem cartItem = new CartItem();
       cartItem.setQuantity(dto.getQuantity());
       cartItem.setTotalPrice(dto.getTotalPrice());
       return cartItem;
    }

    public CartItemDTO toDTO(CartItem cartItem){
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(cartItem.getProduct().getId());
        dto.setUnitPrice(cartItem.getProduct().getPrice());
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotalPrice(cartItem.getTotalPrice());
        return dto;
    }
}
