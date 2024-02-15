package pl.inpost.pricecalculator.exception;

import static java.lang.String.format;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DiscountAlreadyExistsException extends RuntimeException {

    public DiscountAlreadyExistsException(UUID productId) {
        super(format("Discount already exist for product id: %s", productId));
    }
}
