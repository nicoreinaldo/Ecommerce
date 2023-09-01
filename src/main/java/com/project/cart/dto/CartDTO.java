package com.project.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivity;
    private List<ProductDTO> products;
}
