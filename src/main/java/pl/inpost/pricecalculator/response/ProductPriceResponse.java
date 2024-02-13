package pl.inpost.pricecalculator.response;

import java.math.BigDecimal;
import pl.inpost.pricecalculator.entity.Product;

public record ProductPriceResponse(BigDecimal value) {
    public static ProductPriceResponse fromProduct(Product product) {
        return new ProductPriceResponse(product.getBasePrice());
    }
}
