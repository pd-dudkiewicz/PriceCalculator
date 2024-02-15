package pl.inpost.pricecalculator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "discounts", schema = "pricecalculator")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product product;

    private Integer minimumCount;

    private Integer percentage;

    public Discount() {}

    public Discount(Long id, Product product, Integer minimumCount, Integer percentage) {
        this.id = id;
        this.product = product;
        this.minimumCount = minimumCount;
        this.percentage = percentage;
    }

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
