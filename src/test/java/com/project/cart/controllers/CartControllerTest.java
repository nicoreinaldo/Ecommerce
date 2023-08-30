package com.project.cart.controllers;

import com.project.cart.model.Cart;
import com.project.cart.model.Product;
import com.project.cart.repository.CartRepository;
import com.project.cart.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;
    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private CartController cartController;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testCreateCart() throws Exception {
        Cart createdCart = new Cart();
        UUID cartId = UUID.randomUUID();
        createdCart.setId(cartId);
        when(cartService.createCart()).thenReturn(createdCart);

        mockMvc.perform(post("/carts"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdCart.getId().toString()));

        verify(cartService, times(1)).createCart();
    }

    @Test
    void testGetCart() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        when(cartService.getCartById(cartId)).thenReturn(cart);

        mockMvc.perform(get("/carts/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cart.getId()));

        verify(cartService, times(1)).getCartById(cartId);
    }

    public void testAddProductsToCart() throws Exception {
        UUID cartId = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        products.add(new Product("Product 1", 10.0));
        products.add(new Product("Product 2", 15.0));

        Cart cart = new Cart();
        when(cartService.addProductsToCart(eq(cartId), eq(products))).thenReturn(new ResponseEntity<>(cart, HttpStatus.OK));

        mockMvc.perform(post("/api/carts/{cartId}/products", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(products)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cart.getId().toString()));

        verify(cartService, times(1)).addProductsToCart(eq(cartId), eq(products));
    }


    @Test
    void testDeleteCart() throws Exception {
        UUID cartId = UUID.randomUUID();

        mockMvc.perform(delete("/carts/{cartId}", cartId))
                .andExpect(status().isOk());

        verify(cartService, times(1)).deleteCart(cartId);
    }
}
