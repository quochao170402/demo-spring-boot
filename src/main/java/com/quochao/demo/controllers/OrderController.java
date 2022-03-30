package com.quochao.demo.controllers;

import com.quochao.demo.dtos.ResponseObjectDTO;
import com.quochao.demo.entities.Cart;
import com.quochao.demo.mappers.CartMapper;
import com.quochao.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/orders")
public class OrderController {
    private final CartService cartService;

    @Autowired
    public OrderController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ResponseObjectDTO> getOrders() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("OK", "All orders",
                        cartService.findAll().stream()
                                .map(cart -> CartMapper.getInstance().toDTO(cart))
                                .collect(Collectors.toList())));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseObjectDTO> checkOrder(@PathVariable Long id) {
        Cart cart = cartService.checkOrder(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("SUCCESSFUL", "Order processed", CartMapper.getInstance().toDTO(cart)));
    }
}
