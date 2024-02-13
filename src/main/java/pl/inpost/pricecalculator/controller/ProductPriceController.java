package pl.inpost.pricecalculator.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.inpost.pricecalculator.response.ProductPriceResponse;
import pl.inpost.pricecalculator.service.PriceCalculatingService;

@RestController
@RequestMapping("/product")
public class ProductPriceController {

    private final PriceCalculatingService priceCalculatingService;

    @Autowired
    public ProductPriceController(PriceCalculatingService priceCalculatingService) {
        this.priceCalculatingService = priceCalculatingService;
    }

    @GetMapping("/{id}/price")
    @ResponseStatus(OK)
    public ProductPriceResponse getProductPrice(@PathVariable UUID id, @RequestParam Integer count) {
        return priceCalculatingService.calculate(id, count);
    }
}
