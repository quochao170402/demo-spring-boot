package com.quochao.demo.controllers;

import com.quochao.demo.entities.Product;
import com.quochao.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/cart")
public class CartController {

    private final ProductService productService;

    @Autowired
    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> cart(HttpSession session) {
        Map<Long, Product> cart = (Map<Long, Product>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();
        return new ArrayList<>(cart.values());
    }

    @GetMapping(path = "/{id}")
    public Boolean addToCart(HttpSession session, @PathVariable Long id) {
        Map<Long, Product> cart = (Map<Long, Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        Product product = productService.findById(id);

        if (product == null) {
            return false;
        }

        if (cart.containsKey(id)) {
            return true;
        } else {
            cart.put(id, product);
        }
        session.setAttribute("cart", cart);
        return true;
    }
}
