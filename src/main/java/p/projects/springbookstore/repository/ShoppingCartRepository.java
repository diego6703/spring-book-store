package p.projects.springbookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUserId(Long userId);
}
