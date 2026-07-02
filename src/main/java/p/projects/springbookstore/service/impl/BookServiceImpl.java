package p.projects.springbookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.mapper.BookMapper;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.repository.BookRepository;
import p.projects.springbookstore.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
