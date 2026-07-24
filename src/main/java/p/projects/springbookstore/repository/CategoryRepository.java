package p.projects.springbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import p.projects.springbookstore.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
