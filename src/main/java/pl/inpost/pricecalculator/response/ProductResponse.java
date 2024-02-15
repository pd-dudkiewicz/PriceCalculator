package pl.inpost.pricecalculator.response;

import java.math.BigDecimal;
import java.util.UUID;
import pl.inpost.pricecalculator.entity.Product;

public record ProductResponse(UUID id, BigDecimal basePrice, String name) {
    public static ProductResponse fromProduct(Product product) {
        return new ProductResponse(product.getId(), product.getBasePrice(), product.getName());
    }
}
