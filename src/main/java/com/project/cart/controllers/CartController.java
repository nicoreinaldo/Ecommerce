package com.project.cart.controllers;

import com.project.cart.exception.CartException;
import com.project.cart.model.Cart;
import com.project.cart.model.Product;
import com.project.cart.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        try {
            Cart createdCart = cartService.createCart();
            return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
        }catch (CartException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID cartId) {
        Cart cart = cartService.getCartById(cartId);
        if (cart != null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } else {
            return new ResponseEntity("Not found cart with Id: "+cartId.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{cartId}/products")
    public ResponseEntity<Cart> addProductsToCart(@PathVariable UUID cartId, @RequestBody List<Product> products) {
            return cartService.addProductsToCart(cartId, products);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        try {
            cartService.deleteCart(cartId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CartException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}