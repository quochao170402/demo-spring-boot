package com.quochao.demo.controllers;

import com.quochao.demo.dtos.CartDTO;
import com.quochao.demo.dtos.CartItemDTO;
import com.quochao.demo.entities.Cart;
import com.quochao.demo.entities.CartItem;
import com.quochao.demo.entities.Product;
import com.quochao.demo.mappers.CartItemMapper;
import com.quochao.demo.mappers.CartMapper;
import com.quochao.demo.services.CartItemService;
import com.quochao.demo.services.CartService;
import com.quochao.demo.services.ProductService;
import com.quochao.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/carts")
public class CartController {

    private final ProductService productService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;

    @Autowired
    public CartController(ProductService productService, CartService cartService, CartItemService cartItemService, UserService userService) {
        this.productService = productService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @GetMapping
    public List<CartItemDTO> cart(HttpSession session) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();
        return new ArrayList<>(cart.values());
    }

    @GetMapping(path = "/{id}")
    public CartItemDTO addToCart(HttpSession session, @PathVariable Long id) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        Product product = productService.findById(id);
        if (product == null) throw new IllegalStateException("Not found product with id " + id);

        CartItemDTO cartItemDTO = null;
        if (cart.containsKey(product.getId())) {
            cartItemDTO = cart.get(id);
            cartItemDTO.setQuantity(cartItemDTO.getQuantity() + 1);
            cartItemDTO.setTotalPrice(cartItemDTO.getUnitPrice() * cartItemDTO.getQuantity());
        } else {
            cartItemDTO = new CartItemDTO();
            cartItemDTO.setProductId(id);
            cartItemDTO.setUnitPrice(product.getPrice());
            cartItemDTO.setQuantity(1);
            cartItemDTO.setTotalPrice(product.getPrice());
        }
        cart.put(id, cartItemDTO);
        session.setAttribute("cart", cart);
        return cartItemDTO;
    }

    @PutMapping(path = "/{id}")
    public CartItemDTO updateCartItem(
            HttpSession session,
            @PathVariable Long id,
            @RequestParam(name = "quantity", required = false) Integer quantity) {
        if (quantity == null || quantity < 0)
            throw new IllegalStateException("Quantity invalid");
        ;

        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            throw new IllegalStateException("Your cart not exists");
        }

        if (cart.containsKey(id)) {
            CartItemDTO cartItemDTO = cart.get(id);
            if (quantity == 0) {
                cart.remove(id);
                return null;
            } else {
                cartItemDTO.setQuantity(quantity);
                cart.put(id, cartItemDTO);
                return cartItemDTO;
            }
        }
        session.setAttribute("cart", cart);
        throw new IllegalStateException("Product without your cart");
    }

    @DeleteMapping(path = "/{id}")
    public boolean deleteCartItem(
            HttpSession session,
            @PathVariable Long id) {

        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            throw new IllegalStateException("Your cart not exists");
        }

        if (!cart.containsKey(id))
            throw new IllegalStateException("Not found product in cart");
        else cart.remove(id);

        session.setAttribute("cart", cart);
        return true;
    }

    @GetMapping(path = "/checkout")
    public CartDTO checkout(HttpSession session) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        Cart cartEntity = new Cart();
        cartEntity.setUser(userService.getById(1L));
        cartService.createCart(cartEntity);

        List<CartItem> cartItems = cart.values().stream()
                .map(c -> {
                    CartItem cartItem = CartItemMapper.getInstance().toEntity(c);
                    cartItem.setCart(cartEntity);
                    cartItem.setProduct(productService.findById(c.getProductId()));
                    return cartItem;
                })
                .collect(Collectors.toList());
        cartItemService.addAllItems(cartItems);
        cartEntity.setCartItems(cartItems);
        session.removeAttribute("cart");
        return CartMapper.getInstance().toDTO(cartService.updateCart(cartEntity));
    }
}
