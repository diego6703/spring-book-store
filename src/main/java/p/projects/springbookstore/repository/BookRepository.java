package p.projects.springbookstore.repository;

import java.util.List;
import p.projects.springbootbookstore.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
