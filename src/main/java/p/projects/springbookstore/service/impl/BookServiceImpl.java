package p.projects.springbookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.repository.BookRepository;
import p.projects.springbookstore.service.BookService;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
