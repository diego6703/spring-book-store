package p.projects.springbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
