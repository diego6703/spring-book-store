package p.projects.springbookstore.service;

import java.util.List;
import p.projects.springbootbookstore.model.Book;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
