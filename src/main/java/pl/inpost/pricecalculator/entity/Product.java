package pl.inpost.pricecalculator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products", schema = "pricecalculator")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal basePrice;

    private String name;

    public Product() {}

    public Product(UUID id, BigDecimal basePrice, String name) {
        this.id = id;
        this.basePrice = basePrice;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public String getName() {
        return name;
    }
}
