package p.projects.springbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
