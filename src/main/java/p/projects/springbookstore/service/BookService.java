package p.projects.springbookstore.service;

import java.util.List;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.BookSearchParametersDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.dto.UpdateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateBookById(Long id, UpdateBookRequestDto requestDto);

    void deleteBookById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
