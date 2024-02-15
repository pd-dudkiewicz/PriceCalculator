package pl.inpost.pricecalculator.request;

import pl.inpost.pricecalculator.entity.Discount;
import pl.inpost.pricecalculator.entity.Product;

public record DiscountRequest(Integer minimumCount, Integer percentage) {

    public Discount toDiscount(Product product) {
        return new Discount(null, product, minimumCount, percentage);
    }

    public Discount toDiscount(Product product, Long id) {
        return new Discount(id, product, minimumCount, percentage);
    }
}
