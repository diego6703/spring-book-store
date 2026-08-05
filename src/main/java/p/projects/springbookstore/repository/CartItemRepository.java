package p.projects.springbookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    Optional<CartItem> findByIdAndShoppingCartUserId(Long cartItemId, Long userId);
}
