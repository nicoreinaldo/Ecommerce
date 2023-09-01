package com.project.cart.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cart.dto.CartDTO;
import com.project.cart.dto.ProductDTO;
import com.project.cart.dto.ProductRequestDTO;
import com.project.cart.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;
    @InjectMocks
    private CartController cartController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testCreateCart() throws Exception {
        CartDTO createdCart = new CartDTO();
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
        CartDTO cart = new CartDTO();
        when(cartService.getCartById(cartId)).thenReturn(cart);

        mockMvc.perform(get("/carts/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cart.getId()));

        verify(cartService, times(1)).getCartById(cartId);
    }


    @Transactional
    @DirtiesContext
    @Test
    public void testAddProductsToCart() throws Exception {
        UUID cartId = UUID.randomUUID();
        List<ProductRequestDTO> products = new ArrayList<>();
        products.add(new ProductRequestDTO("Product 1", 10.0));
        products.add(new ProductRequestDTO("Product 2", 15.0));

        List<ProductDTO> productsDTO = new ArrayList<>();
        productsDTO.add(new ProductDTO(1,"Product 1", 10.0));
        productsDTO.add(new ProductDTO(2,"Product 2", 15.0));

        CartDTO cartDTO = new CartDTO(cartId, LocalDateTime.now(), LocalDateTime.now(), productsDTO);
        ResponseEntity<CartDTO> responseEntity = ResponseEntity.ok(cartDTO);

        when(cartService.addProductsToCart(any(UUID.class), anyList())).thenReturn(responseEntity);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/carts/{cartId}/products", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(products));

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(cartService, times(1)).addProductsToCart(any(UUID.class), anyList());
    }


    @Test
    void testDeleteCart() throws Exception {
        UUID cartId = UUID.randomUUID();

        mockMvc.perform(delete("/carts/{cartId}", cartId))
                .andExpect(status().isOk());

        verify(cartService, times(1)).deleteCart(cartId);
    }

    @Test
    void testGetNonExistentCart() throws Exception {
        UUID cartId = UUID.randomUUID();
        when(cartService.getCartById(cartId)).thenReturn(null);

        mockMvc.perform(get("/carts/{cartId}", cartId))
                .andExpect(status().isBadRequest());

        verify(cartService, times(1)).getCartById(cartId);
    }


    private String toJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
