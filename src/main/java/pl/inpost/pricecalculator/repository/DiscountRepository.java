package pl.inpost.pricecalculator.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import pl.inpost.pricecalculator.entity.Discount;

public interface DiscountRepository extends CrudRepository<Discount, String> {
    Optional<Discount> findByProductIdAndMinimumCountIsLessThanEqual(UUID productId, Integer count);
}
