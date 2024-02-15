package pl.inpost.pricecalculator.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import pl.inpost.pricecalculator.entity.Discount;

public interface DiscountRepository extends CrudRepository<Discount, Long> {
    Optional<Discount> findByProductId(UUID productId);

    Optional<Discount> findByProductIdAndId(UUID productId, Long id);

    Optional<Discount> findByProductIdAndMinimumCountIsLessThanEqual(UUID productId, Integer count);

}
