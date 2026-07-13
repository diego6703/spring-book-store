package p.projects.springbookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.BookSearchParametersDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.dto.UpdateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto updateBookById(Long id, UpdateBookRequestDto requestDto);

    void deleteBookById(Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
