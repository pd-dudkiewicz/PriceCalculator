package pl.inpost.pricecalculator.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.inpost.pricecalculator.request.DiscountRequest;
import pl.inpost.pricecalculator.response.DiscountResponse;
import pl.inpost.pricecalculator.service.DiscountService;

@RestController
@RequestMapping("/product/{productId}/discount")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public DiscountResponse create(@PathVariable UUID productId, @RequestBody DiscountRequest discountRequest) {
        return discountService.create(productId, discountRequest);
    }

    @GetMapping
    @ResponseStatus(OK)
    public DiscountResponse get(@PathVariable UUID productId) {
        return discountService.get(productId);
    }


    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public DiscountResponse update(@PathVariable UUID productId, @PathVariable Long id, @RequestBody DiscountRequest discountRequest) {
        return discountService.update(productId, id, discountRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID productId, @PathVariable Long id) {
        discountService.delete(productId, id);
    }
}
