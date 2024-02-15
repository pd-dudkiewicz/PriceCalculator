package pl.inpost.pricecalculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.inpost.pricecalculator.entity.Discount;
import pl.inpost.pricecalculator.entity.Product;
import pl.inpost.pricecalculator.exception.ProductNotFoundException;
import pl.inpost.pricecalculator.repository.DiscountRepository;
import pl.inpost.pricecalculator.repository.ProductRepository;
import pl.inpost.pricecalculator.response.ProductPriceResponse;

@ExtendWith(MockitoExtension.class)
public class PriceCalculatingServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PriceCalculatingService priceCalculatingService;

    @Test
    void calculate_givenProductWithDiscountHasBeenFound_shouldReturnDiscountedPrice() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, BigDecimal.valueOf(100), "test product");
        Discount discount = new Discount(1L, product, 4, 20);
        when(discountRepository.findByProductIdAndMinimumCountIsLessThanEqual(any(), any()))
            .thenReturn(Optional.of(discount));

        ProductPriceResponse productPriceResponse = priceCalculatingService.calculate(productId, 4);

        assertThat(productPriceResponse.value()).isEqualTo(BigDecimal.valueOf(80));
        verify(discountRepository).findByProductIdAndMinimumCountIsLessThanEqual(productId, 4);
        verifyNoInteractions(productRepository);
    }

    @Test
    void calculate_givenProductWithNoDiscountHasBeenFound_shouldReturnBasePrice() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, BigDecimal.valueOf(100), "test product");
        when(discountRepository.findByProductIdAndMinimumCountIsLessThanEqual(any(), any()))
            .thenReturn(Optional.empty());
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        ProductPriceResponse productPriceResponse = priceCalculatingService.calculate(productId, 4);

        assertThat(productPriceResponse.value()).isEqualTo(BigDecimal.valueOf(100));
        verify(discountRepository).findByProductIdAndMinimumCountIsLessThanEqual(productId, 4);
        verify(productRepository).findById(productId);
    }

    @Test
    void calculate_givenNoProductHasBeenFound_shouldThrowProductNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(discountRepository.findByProductIdAndMinimumCountIsLessThanEqual(any(), any()))
            .thenReturn(Optional.empty());
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> priceCalculatingService.calculate(productId, 4))
            .isInstanceOf(ProductNotFoundException.class);
        verify(discountRepository).findByProductIdAndMinimumCountIsLessThanEqual(productId, 4);
        verify(productRepository).findById(productId);
    }
}
