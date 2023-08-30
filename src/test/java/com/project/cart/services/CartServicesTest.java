package com.project.cart.services;

import com.project.cart.model.Cart;
import com.project.cart.model.Product;
import com.project.cart.repository.CartRepository;
import com.project.cart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
public class CartServicesTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() {
        Cart expectedCart = new Cart();
        when(cartRepository.save(any())).thenReturn(expectedCart);

        Cart createdCart = cartService.createCart();

        assertNotNull(createdCart);
        assertEquals(expectedCart, createdCart);
        verify(cartRepository, times(1)).save(any());
    }

    @Test
    void testGetCartById() {
        UUID cartId = UUID.randomUUID();
        Cart cart = Mockito.mock(Cart.class);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Cart retrievedCart = cartService.getCartById(cartId);

        assertNotNull(retrievedCart);
        assertEquals(cart, retrievedCart);
        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    void testGetAllCarts() {
        List<Cart> expectedCarts = new ArrayList<>();
        expectedCarts.add(new Cart());
        when(cartRepository.findAll()).thenReturn(expectedCarts);

        List<Cart> retrievedCarts = cartService.getAllCarts();

        assertNotNull(retrievedCarts);
        assertEquals(expectedCarts.size(), retrievedCarts.size());
        verify(cartRepository, times(1)).findAll();
    }

    @Transactional
    @DirtiesContext
    @Test
    void testAddProductsToCart() {
        UUID cartId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        products.add(new Product("Product 1", 10.0));
        products.add(new Product("Product 2", 15.0));

        Cart cart = new Cart();
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        // Capture the LocalDateTime before making the call
        LocalDateTime beforeCall = LocalDateTime.now();

        ResponseEntity<Cart> responseEntity = cartService.addProductsToCart(cartId, products);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, times(1)).save(cart);

        //use assertj - assertThat for compare te last LocalDateTime with tolerance that 2 seconds
        assertThat(cart.getLastActivity()).isCloseTo(beforeCall, within(2, ChronoUnit.SECONDS));
        assertEquals(cart.getProducts().size(), 2);
    }

    @Test
    void testAddProductsToCartNotFound() {
        UUID cartId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        products.add(new Product("Product 1", 10.0));
        products.add(new Product("Product 2", 15.0));

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        ResponseEntity<Cart> responseEntity = cartService.addProductsToCart(cartId, products);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testAddProductsToCartException() {
        UUID cartId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();

        when(cartRepository.findById(cartId)).thenThrow(new RuntimeException("Simulated exception"));

        ResponseEntity<Cart> responseEntity = cartService.addProductsToCart(cartId, products);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        verify(cartRepository, times(1)).findById(cartId);
        verify(cartRepository, never()).save(any());
    }


    @Test
    void testDeleteCart() {
        UUID cartId = UUID.randomUUID();

        assertDoesNotThrow(() -> cartService.deleteCart(cartId));

        verify(cartRepository, times(1)).deleteById(cartId);
    }

}
