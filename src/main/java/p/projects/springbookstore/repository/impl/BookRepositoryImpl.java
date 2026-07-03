package p.projects.springbookstore.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return entityManager.createQuery("SELECT b FROM Book b", Book.class)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        Book book = entityManager.find(Book.class, id);
        return Optional.ofNullable(book);
    }
}
