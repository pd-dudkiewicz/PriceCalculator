package pl.inpost.pricecalculator.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.inpost.pricecalculator.request.ProductRequest;
import pl.inpost.pricecalculator.response.ProductResponse;
import pl.inpost.pricecalculator.service.ProductService;

@RestController
public class ProductController {
    
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    @ResponseStatus(CREATED)
    public ProductResponse create(@RequestBody @Valid ProductRequest productRequest) {
        return productService.create(productRequest);
    }

    @GetMapping("/products")
    @ResponseStatus(OK)
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @PutMapping("/product/{id}")
    @ResponseStatus(OK)
    public ProductResponse update(@PathVariable UUID id, @RequestBody @Valid ProductRequest productRequest) {
        return productService.update(id, productRequest);
    }

    @DeleteMapping("/product/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.delete(id);
    }
}
