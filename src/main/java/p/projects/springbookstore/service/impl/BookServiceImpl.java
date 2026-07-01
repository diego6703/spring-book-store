package p.projects.springbookstore.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import p.projects.springbootbookstore.model.Book;
import p.projects.springbootbookstore.repository.BookRepository;
import p.projects.springbootbookstore.service.BookService;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
