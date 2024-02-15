package pl.inpost.pricecalculator.exception;

public class DiscountNotFoundException extends RuntimeException {

    public DiscountNotFoundException() {
        super("Discount not found");
    }
}
