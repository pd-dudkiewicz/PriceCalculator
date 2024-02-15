package pl.inpost.pricecalculator.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.inpost.pricecalculator.exception.ProductNotFoundException;
import pl.inpost.pricecalculator.repository.ProductRepository;
import pl.inpost.pricecalculator.request.ProductRequest;
import pl.inpost.pricecalculator.response.ProductResponse;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest productRequest) {
        var product = productRepository.save(productRequest.toProduct());

        return ProductResponse.fromProduct(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
            .map(ProductResponse::fromProduct)
            .toList();
    }

    public ProductResponse update(UUID id, ProductRequest productRequest) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException();
        }
        var updatedProduct = productRepository.save(productRequest.toProduct());

        return ProductResponse.fromProduct(updatedProduct);
    }

    public void delete(UUID id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(id);
    }
}
