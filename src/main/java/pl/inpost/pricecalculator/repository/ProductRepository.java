package pl.inpost.pricecalculator.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import pl.inpost.pricecalculator.entity.Product;

public interface ProductRepository extends CrudRepository<Product, UUID> {

    @Override
    List<Product> findAll();
}
