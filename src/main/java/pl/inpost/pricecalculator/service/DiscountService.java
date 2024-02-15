package pl.inpost.pricecalculator.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.inpost.pricecalculator.exception.DiscountAlreadyExistsException;
import pl.inpost.pricecalculator.exception.DiscountNotFoundException;
import pl.inpost.pricecalculator.exception.ProductNotFoundException;
import pl.inpost.pricecalculator.repository.DiscountRepository;
import pl.inpost.pricecalculator.repository.ProductRepository;
import pl.inpost.pricecalculator.request.DiscountRequest;
import pl.inpost.pricecalculator.response.DiscountResponse;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    public DiscountResponse create(UUID productId, DiscountRequest discountRequest) {
        var product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ProductNotFoundException();
        }
        if (discountRepository.findByProductId(productId).isPresent()) {
            throw new DiscountAlreadyExistsException(productId);
        }
        var discount = discountRepository.save(discountRequest.toDiscount(product.get()));

        return DiscountResponse.fromDiscount(discount);
    }

    public DiscountResponse get(UUID productId) {
        return discountRepository.findByProductId(productId)
            .map(DiscountResponse::fromDiscount)
            .orElseThrow(DiscountNotFoundException::new);
    }

    public DiscountResponse update(UUID productId, Long id, DiscountRequest discountRequest) {
        var discount = discountRepository.findByProductIdAndId(productId, id);
        if (discount.isEmpty()) {
            throw new DiscountNotFoundException();
        }
        var updatedDiscount = discountRepository.save(discountRequest.toDiscount(discount.get().getProduct(), id));

        return DiscountResponse.fromDiscount(updatedDiscount);
    }

    public void delete(UUID productId, Long id) {
        if (discountRepository.findByProductIdAndId(productId, id).isEmpty()) {
            throw new DiscountNotFoundException();
        }
        discountRepository.deleteById(id);
    }
}
