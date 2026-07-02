package p.projects.springbookstore.service;

import java.util.List;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.model.Book;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    List<Book> findAll();
}
