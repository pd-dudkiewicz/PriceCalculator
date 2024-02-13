package pl.inpost.pricecalculator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "discounts", schema = "pricecalculator")
public class Discount {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product product;

    private Integer minimumCount;

    private Integer percentage;

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getMinimumCount() {
        return minimumCount;
    }

    public Integer getPercentage() {
        return percentage;
    }
}
