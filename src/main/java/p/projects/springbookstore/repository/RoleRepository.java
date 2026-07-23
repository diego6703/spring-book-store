package p.projects.springbookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.Role;
import p.projects.springbookstore.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
