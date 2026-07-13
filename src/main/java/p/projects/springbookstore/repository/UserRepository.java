package p.projects.springbookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
