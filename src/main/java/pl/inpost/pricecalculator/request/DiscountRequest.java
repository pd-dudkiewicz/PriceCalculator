package pl.inpost.pricecalculator.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import pl.inpost.pricecalculator.entity.Discount;
import pl.inpost.pricecalculator.entity.Product;

public record DiscountRequest(
    @Min(value = 1, message = "Minimum count cannot be less than 1")
    Integer minimumCount,
    @Min(value = 1, message = "Percentage cannot be less than 1")
    @Max(value = 99, message = "Percentage cannot be greater than 99")
    Integer percentage
) {

    public Discount toDiscount(Product product) {
        return new Discount(null, product, minimumCount, percentage);
    }

    public Discount toDiscount(Product product, Long id) {
        return new Discount(id, product, minimumCount, percentage);
    }
}
