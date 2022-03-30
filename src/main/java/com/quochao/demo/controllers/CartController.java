package com.quochao.demo.controllers;

import com.quochao.demo.dtos.CartItemDTO;
import com.quochao.demo.dtos.ResponseObjectDTO;
import com.quochao.demo.entities.Cart;
import com.quochao.demo.entities.CartItem;
import com.quochao.demo.entities.Product;
import com.quochao.demo.exceptions.APIException;
import com.quochao.demo.exceptions.ResourceNotFoundException;
import com.quochao.demo.mappers.CartItemMapper;
import com.quochao.demo.mappers.CartMapper;
import com.quochao.demo.services.CartItemService;
import com.quochao.demo.services.CartService;
import com.quochao.demo.services.ProductService;
import com.quochao.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
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
    public ResponseEntity<ResponseObjectDTO> cart(HttpSession session) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        return (cart == null) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObjectDTO("NOT FOUND", "Not found cart", null)) :
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObjectDTO("OK", "All items in cart",
                        new ArrayList<>(cart.values())));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseObjectDTO> addToCart(HttpSession session, @PathVariable Long id) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        Product product = productService.findById(id);
        if (product == null || product.getQuantity() == 0) {
            throw new ResourceNotFoundException("Not found product with id " + id);
        }

        CartItemDTO cartItemDTO;
        if (cart.containsKey(id) ) {
            if (cart.get(id).getQuantity().equals(product.getQuantity())) throw new ResourceNotFoundException("Not found product with id "+id);
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
//        productService.updateQuantityById(id, 1);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("SUCCESSFUL", "Added items to cart", cartItemDTO));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponseObjectDTO> updateCartItem(
            HttpSession session,
            @PathVariable Long id,
            @RequestParam(name = "quantity", required = false) Integer quantity) {
        if (quantity == null || quantity < 0)
            throw new APIException("Quantity of items must positive");

        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObjectDTO("NOT FOUND", "Not found cart", null));

        Product product = productService.findById(id);
        if (product == null || product.getQuantity() < quantity)
            throw new ResourceNotFoundException("Not found product with id " + id);

        if (cart.containsKey(id)) {
            CartItemDTO cartItemDTO = cart.get(id);
            if (quantity == 0) {
                cart.remove(id);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObjectDTO("DELETED", "Deleted items", null));
            } else {
                cartItemDTO.setQuantity(quantity);
                cart.put(id, cartItemDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObjectDTO("UPDATED", "Updated quantity for cart items", cartItemDTO));
            }
        }
        session.setAttribute("cart", cart);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObjectDTO("NOT FOUND", "Not found item with id " + id + " in your cart", null));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ResponseObjectDTO> deleteCartItem(
            HttpSession session,
            @PathVariable Long id) {

        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObjectDTO("NOT FOUND", "Not found cart", false));
        }

        if (!cart.containsKey(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObjectDTO("NOT FOUND", "Not found item in your cart", false));
        else cart.remove(id);

        session.setAttribute("cart", cart);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("DELETED", "Deleted items", true));
    }

    @GetMapping(path = "/checkout")
    public ResponseEntity<ResponseObjectDTO> checkout(HttpSession session) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObjectDTO("NOT FOUND", "Not found cart", false));
        }

        Cart cartEntity = new Cart();
        cartEntity.setUser(userService.getById(1L));
        cartService.createCart(cartEntity);

        List<CartItem> cartItems = cart.values().stream()
                .map(c -> {
                    CartItem cartItem = CartItemMapper.getInstance().toEntity(c);
                    cartItem.setCart(cartEntity);
                    cartItem.setProduct(productService.findById(c.getProductId()));
                    productService.updateQuantityById(cartItem.getProduct().getId(), cartItem.getQuantity());
                    return cartItem;
                })
                .collect(Collectors.toList());

        if (cartItems.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObjectDTO("FAILED", "Cart empty so cannot checkout", null));

        cartItemService.addAllItems(cartItems);
        cartEntity.setCartItems(cartItems);
        session.removeAttribute("cart");

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("SUCCESSFUL", "Checkout successful",
                        CartMapper.getInstance().toDTO(cartService.updateCart(cartEntity))));
    }
}
