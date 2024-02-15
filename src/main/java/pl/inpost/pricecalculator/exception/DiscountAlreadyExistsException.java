package pl.inpost.pricecalculator.exception;

import static java.lang.String.format;

import java.util.UUID;

public class DiscountAlreadyExistsException extends RuntimeException {

    public DiscountAlreadyExistsException(UUID productId) {
        super(format("Discount already exist for product id: %s", productId));
    }
}
