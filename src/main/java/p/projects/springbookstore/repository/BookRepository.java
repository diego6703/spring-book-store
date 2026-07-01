package p.projects.springbookstore.repository;

import java.util.List;
import p.projects.springbookstore.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
