package com.project.cart.services;

import com.project.cart.exception.CartException;
import com.project.cart.exception.CartNotFoundException;
import com.project.cart.model.Cart;
import com.project.cart.model.Product;
import com.project.cart.repository.CartRepository;
import com.project.cart.repository.ProductRepository;
import com.project.cart.task.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(CartService.class);


    public Cart createCart() {
        try {
            Cart cart = new Cart();
            return cartRepository.save(cart);
        } catch (Exception e) {
            log.info("Error creating cart {} ", e);
            throw new CartException("Error creating cart", e);
        }
    }

    public Cart getCartById(UUID cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public ResponseEntity<Cart> addProductsToCart(UUID cartId, List<Product> products) {
        try {
            Optional<Cart> cart = cartRepository.findById(cartId);

            if (cart.isPresent()) {
            saveUUIDinProduct(products,cart.get());

            cart.get().getProducts().addAll(products);
            cart.get().setLastActivity(LocalDateTime.now());
            Cart updatedCart = cartRepository.save(cart.get());

            log.info("Update cart {} ", updatedCart);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);

            } else {
                log.info("Cart not found {} ", cartId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error try add product in cart {}, error {} ", cartId ,e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteCart(UUID cartId) {
        try {
            cartRepository.deleteById(cartId);
            log.info("Deleted carts complete {}", cartId);
        } catch (Exception e) {
            log.info("Deleted carts complete {}, error {} ", cartId, e);
            throw new CartException("Error deleting cart with ID: " + cartId, e);
        }
    }

    private List<Product> saveUUIDinProduct(List<Product> products,Cart cart){
        for (Product product : products) {
            product.setCart(cart);
            productRepository.save(product);
            log.info("Save Product {} ", product);
        }
        return products;
    }

}

