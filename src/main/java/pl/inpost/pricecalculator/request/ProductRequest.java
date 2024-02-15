package pl.inpost.pricecalculator.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import pl.inpost.pricecalculator.entity.Product;

public record ProductRequest(
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "999999.99")
    @Digits(integer=6, fraction=2)
    BigDecimal basePrice,

    @Size(max = 64)
    String name
) {

    public Product toProduct() {
        return new Product(null, basePrice, name);
    }
}
