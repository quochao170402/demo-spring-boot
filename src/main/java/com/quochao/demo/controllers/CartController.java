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
    private final UserService userService;

    @Autowired
    public CartController(ProductService productService, CartService cartService, CartItemService cartItemService, UserService userService) {
        this.productService = productService;
        this.cartService = cartService;
        this.userService = userService;
    }

    /*Work with shopping cart (this cart is a hash map has key is a product id and value is a cart item)
    step 1 : Get cart from session, check cart existed
    step 2 : crud with cart
    step 3 : put cart to session
    * */

    @GetMapping
    public ResponseEntity<ResponseObjectDTO> cart(HttpSession session) {
        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        return (cart == null) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObjectDTO("NOT FOUND", "Not found cart", null)) :
                ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObjectDTO("OK", "All items in cart", new ArrayList<>(cart.values())));
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
//        If cart contains cart item to increasing quantity of item in cart (default increasing quantity 1)
//        else create new cart item and add it to cart (default quantity of new cart item is 1)
        if (cart.containsKey(id)) {
//            Check inventory of product in warehouse, if inventory is less quantity to cannot add to cart
            if (cart.get(id).getQuantity().equals(product.getQuantity()))
                throw new ResourceNotFoundException("Not found product with id " + id);
            cartItemDTO = cart.get(id);
            cartItemDTO.setQuantity(cartItemDTO.getQuantity() + 1);
        } else {
            cartItemDTO = new CartItemDTO();
            cartItemDTO.setProductId(id);
            cartItemDTO.setUnitPrice(product.getPrice());
            cartItemDTO.setQuantity(1);
        }

        cart.put(id, cartItemDTO);
        session.setAttribute("cart", cart);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("SUCCESSFUL", "Added items to cart", cartItemDTO));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponseObjectDTO> updateCartItem(
            HttpSession session,
            @PathVariable Long id,
            @RequestParam(name = "quantity", required = false) Integer quantity) {
//        Check validation of quantity
        if (quantity == null || quantity < 0)
            throw new APIException("Quantity of items must positive");

        Map<Long, CartItemDTO> cart = (Map<Long, CartItemDTO>) session.getAttribute("cart");
        if (cart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObjectDTO("NOT FOUND", "Not found cart", null));

        Product product = productService.findById(id);
        if (product == null || product.getQuantity() < quantity)
            throw new ResourceNotFoundException("Not found product with id " + id);

//        Item without cart
        if (!cart.containsKey(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObjectDTO("NOT FOUND", "Not found item with id " + id + " in your cart", null));
        else {
            CartItemDTO cartItemDTO = cart.get(id);
            if (quantity == 0) {
                cart.remove(id);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObjectDTO("DELETED", "Deleted items", null));
            } else {
                cartItemDTO.setQuantity(quantity);
                cart.put(id, cartItemDTO);
                session.setAttribute("cart", cart);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObjectDTO("UPDATED", "Updated quantity for cart items", cartItemDTO));
            }
        }
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

        if (cart.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObjectDTO("FAILED", "Cart empty so cannot checkout", null));

//        Get all items from cart then transfer them to CartItem entity
        List<CartItem> cartItems = cart.values().stream()
                .map(c -> {
                    CartItem cartItem = CartItemMapper.getInstance().toEntity(c);
                    cartItem.setProduct(productService.findById(c.getProductId()));
                    productService.updateQuantityById(cartItem.getProduct().getId(), cartItem.getQuantity());
                    return cartItem;
                })
                .collect(Collectors.toList());

        Cart cartEntity = new Cart();
        cartEntity.setCartItems(cartItems);
        cartEntity.setUser(userService.getById(1L));

        session.removeAttribute("cart");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("SUCCESSFUL", "Checkout successful",
                        CartMapper.getInstance().toDTO(cartService.checkout(cartEntity))));
    }


}
