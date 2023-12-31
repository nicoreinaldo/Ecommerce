package com.project.cart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;

    public Product(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public Product(String description, double amount, Cart cart) {
        this.description = description;
        this.amount = amount;
        this.cart = cart;
    }
}
