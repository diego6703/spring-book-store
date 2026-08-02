package p.projects.springbookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
}
