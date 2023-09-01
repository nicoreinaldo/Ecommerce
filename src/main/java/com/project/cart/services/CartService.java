package com.project.cart.services;

import com.project.cart.dto.CartDTO;
import com.project.cart.dto.ProductDTO;
import com.project.cart.dto.ProductRequestDTO;
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
import java.util.ArrayList;
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


    public CartDTO createCart() {
        try {
            Cart cart = new Cart();
            cart = cartRepository.save(cart);
            return convertCartToDTO(cart);
        } catch (Exception e) {
            log.info("Error creating cart {} ", e);
            throw new CartException("Error creating cart", e);
        }
    }

    public CartDTO getCartById(UUID cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.isPresent()) {
            CartDTO cartDTO = this.convertCartToDTO(cart.get());
            return cartDTO;
        } else {
            return null;
        }
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public ResponseEntity<CartDTO> addProductsToCart(UUID cartId, List<ProductRequestDTO> products) {
        try {
            Optional<Cart> cart = cartRepository.findById(cartId);

            if (cart.isPresent()) {
                List<Product> listProducts = saveUUIDinProduct(products,cart.get());

                cart.get().getProducts().addAll(listProducts);
                cart.get().setLastActivity(LocalDateTime.now());
                Cart updatedCart = cartRepository.save(cart.get());

                CartDTO updatedCartDTO = new CartDTO(updatedCart.getId(),
                        updatedCart.getCreatedAt(), updatedCart.getLastActivity(),
                        this.convertProductToDTO(updatedCart.getProducts()));

                log.info("Update cart {} ", updatedCartDTO);
                return new ResponseEntity<>(updatedCartDTO, HttpStatus.OK);

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

    private List<Product> saveUUIDinProduct(List<ProductRequestDTO> products,Cart cart){
        List<Product> listProduct = new ArrayList<>();

        for (ProductRequestDTO productRequest : products) {
            Product product = new Product(productRequest.getDescription(), productRequest.getAmount(), cart);
            listProduct.add(product);
            productRepository.save(product);
            log.info("Save Product {} ", product);
        }
        return listProduct;
    }

    private CartDTO convertCartToDTO(Cart cart) {
        CartDTO dto = new CartDTO(cart.getId(), cart.getCreatedAt(),
                cart.getLastActivity(),this.convertProductToDTO(cart.getProducts()));
        return dto;
    }

    private List<ProductDTO> convertProductToDTO(List<Product> products) {
        List<ProductDTO> list = new ArrayList<>();
        for (Product product : products) {
            ProductDTO dto = new ProductDTO(product.getId(), product.getDescription(), product.getAmount());
            list.add(dto);
        }
        return list;
    }

}

