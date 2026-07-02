package p.projects.springbookstore.repository;

import java.util.List;
import java.util.Optional;
import p.projects.springbookstore.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
