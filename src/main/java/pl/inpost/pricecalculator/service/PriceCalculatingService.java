package pl.inpost.pricecalculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.inpost.pricecalculator.entity.Discount;
import pl.inpost.pricecalculator.exception.ProductNotFoundException;
import pl.inpost.pricecalculator.repository.DiscountRepository;
import pl.inpost.pricecalculator.repository.ProductRepository;
import pl.inpost.pricecalculator.response.ProductPriceResponse;

@Service
public class PriceCalculatingService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    @Autowired
    public PriceCalculatingService(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    public ProductPriceResponse calculate(UUID productId, Integer count) {
        var discount = discountRepository.findByProductIdAndMinimumCountIsLessThanEqual(productId, count);
        if (discount.isEmpty()) {
            return productRepository.findById(productId)
                .map(ProductPriceResponse::fromProduct)
                .orElseThrow(ProductNotFoundException::new);
        }

        return discount.map(this::calculateDiscountedPrice)
            .map(ProductPriceResponse::new)
            .get();
    }

    private BigDecimal calculateDiscountedPrice(Discount discount) {
        return discount.getProduct().getBasePrice()
            .multiply(BigDecimal.valueOf(100 - discount.getPercentage()))
            .divide(BigDecimal.valueOf(100), RoundingMode.UP);
    }
}
