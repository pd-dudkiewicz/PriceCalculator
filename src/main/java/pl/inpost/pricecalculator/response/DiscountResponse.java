package pl.inpost.pricecalculator.response;

import pl.inpost.pricecalculator.entity.Discount;

public record DiscountResponse(Long id, Integer minimumCount, Integer percentage) {
    public static DiscountResponse fromDiscount(Discount discount) {
        return new DiscountResponse(
            discount.getId(),
            discount.getMinimumCount(),
            discount.getPercentage());
    }
}
