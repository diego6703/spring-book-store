package p.projects.springbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import p.projects.springbookstore.model.Book;

public interface BookRepository extends JpaRepository<Book,Long> {
}
