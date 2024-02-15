package pl.inpost.pricecalculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import pl.inpost.pricecalculator.exception.DiscountAlreadyExistsException;
import pl.inpost.pricecalculator.exception.DiscountNotFoundException;
import pl.inpost.pricecalculator.exception.ProductNotFoundException;
import pl.inpost.pricecalculator.repository.DiscountRepository;
import pl.inpost.pricecalculator.repository.ProductRepository;
import pl.inpost.pricecalculator.request.DiscountRequest;
import pl.inpost.pricecalculator.response.DiscountResponse;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DiscountService discountService;

    @Test
    void create_givenDiscountRequestForProductWithNoDiscount_shouldCreateDiscount() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, BigDecimal.valueOf(100), "test product");
        Discount discount = new Discount(1L, product, 4, 20);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(discountRepository.findByProductId(any())).thenReturn(Optional.empty());
        when(discountRepository.save(any())).thenReturn(discount);

        DiscountResponse discountResponse = discountService.create(productId, new DiscountRequest(4, 20));

        assertThat(discountResponse.minimumCount()).isEqualTo(4);
        assertThat(discountResponse.percentage()).isEqualTo(20);
        verify(productRepository).findById(productId);
        verify(discountRepository).findByProductId(productId);
        verify(discountRepository).save(new Discount(null, product, 4, 20));
    }

    @Test
    void create_givenDiscountRequestForNonExistentProduct_shouldThrowProductNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> discountService.create(productId, new DiscountRequest(4, 20)))
            .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findById(productId);
        verifyNoInteractions(discountRepository);
    }

    @Test
    void create_givenDiscountRequestForProductWithDiscount_shouldThrowDiscountAlreadyExistsException() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
        when(discountRepository.findByProductId(any())).thenReturn(Optional.of(new Discount()));

        assertThatThrownBy(() -> discountService.create(productId, new DiscountRequest(4, 20)))
            .isInstanceOf(DiscountAlreadyExistsException.class);

        verify(productRepository).findById(productId);
        verify(discountRepository).findByProductId(productId);
        verifyNoMoreInteractions(discountRepository);
    }

    @Test
    void get_givenProductWithDiscount_shouldReturnDiscount() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, BigDecimal.valueOf(100), "test product");
        Discount discount = new Discount(1L, product, 4, 20);
        when(discountRepository.findByProductId(any())).thenReturn(Optional.of(discount));

        DiscountResponse discountResponse = discountService.get(productId);

        assertThat(discountResponse.id()).isEqualTo(1);
        assertThat(discountResponse.minimumCount()).isEqualTo(4);
        assertThat(discountResponse.percentage()).isEqualTo(20);
        verify(discountRepository).findByProductId(productId);
    }

    @Test
    void get_givenProductWithNoDiscount_shouldThrowDiscountNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(discountRepository.findByProductId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> discountService.get(productId))
            .isInstanceOf(DiscountNotFoundException.class);
        verify(discountRepository).findByProductId(productId);
    }

    @Test
    void update_givenDiscountRequestForProductWithDiscount_shouldUpdateDiscount() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, BigDecimal.valueOf(100), "test product");
        Discount discount = new Discount(1L, product, 4, 20);
        Discount updatedDiscount = new Discount(1L, product, 6, 50);
        when(discountRepository.findByProductIdAndId(any(), any())).thenReturn(Optional.of(discount));
        when(discountRepository.save(any())).thenReturn(updatedDiscount);

        DiscountResponse discountResponse = discountService.update(productId, 1L, new DiscountRequest(6, 50));

        assertThat(discountResponse.minimumCount()).isEqualTo(6);
        assertThat(discountResponse.percentage()).isEqualTo(50);
        verify(discountRepository).findByProductIdAndId(productId, 1L);
        verify(discountRepository).save(updatedDiscount);
    }

    @Test
    void update_givenDiscountRequestForProductWithNoDiscount_shouldThrowDiscountNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(discountRepository.findByProductIdAndId(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> discountService.update(productId, 1L, new DiscountRequest(6, 50)))
            .isInstanceOf(DiscountNotFoundException.class);

        verify(discountRepository).findByProductIdAndId(productId, 1L);
        verifyNoMoreInteractions(discountRepository);
    }

    @Test
    void delete_givenDiscountRequestForProductWithDiscount_shouldDeleteDiscount() {
        UUID productId = UUID.randomUUID();
        when(discountRepository.findByProductIdAndId(any(), any())).thenReturn(Optional.of(new Discount()));

        discountService.delete(productId, 1L);

        verify(discountRepository).findByProductIdAndId(productId, 1L);
        verify(discountRepository).deleteById(1L);
    }

    @Test
    void delete_givenDiscountRequestForProductWithNoDiscount_shouldThrowDiscountNotFoundException() {
        UUID productId = UUID.randomUUID();
        when(discountRepository.findByProductIdAndId(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> discountService.delete(productId, 1L))
            .isInstanceOf(DiscountNotFoundException.class);

        verify(discountRepository).findByProductIdAndId(productId, 1L);
        verifyNoMoreInteractions(discountRepository);
    }
}
