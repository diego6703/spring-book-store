package p.projects.springbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
