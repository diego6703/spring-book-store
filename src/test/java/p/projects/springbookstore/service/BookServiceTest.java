package p.projects.springbookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.BookDtoWithoutCategoryIds;
import p.projects.springbookstore.dto.BookSearchParametersDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.dto.UpdateBookRequestDto;
import p.projects.springbookstore.exception.EntityNotFoundException;
import p.projects.springbookstore.mapper.BookMapper;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.repository.BookRepository;
import p.projects.springbookstore.repository.CategoryRepository;
import p.projects.springbookstore.repository.builder.BookSpecificationBuilder;
import p.projects.springbookstore.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Should save a book successfully")
    void save_ValidRequest_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Clean Code", "Robert C. Martin", "1234567890123",
                BigDecimal.valueOf(29.99), "Desc", "url", Set.of(1L)
        );
        Book book = new Book();
        Book savedBook = new Book();
        BookDto expectedDto = new BookDto(
                1L, "Clean Code", "Robert C. Martin", "1234567890123",
                BigDecimal.valueOf(29.99), "Desc", "url", Set.of(1L)
        );

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto actualDto = bookService.save(requestDto);

        assertThat(actualDto).isNotNull();
        assertThat(actualDto.getTitle()).isEqualTo("Clean Code");
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Should return page of books when findAll is called")
    void findAll_ValidPageable_ReturnsPageOfBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        BookDto bookDto = new BookDto(
                1L, "Clean Code", "Robert C. Martin", "1234567890123",
                BigDecimal.valueOf(29.99), "Desc", "url", Set.of(1L)
        );
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actualPage = bookService.findAll(pageable);

        assertThat(actualPage).isNotNull();
        assertThat(actualPage.getTotalElements()).isEqualTo(1);
        assertThat(actualPage.getContent().get(0).getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @DisplayName("Should return book by ID when book exists")
    void getBookById_ExistingId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        BookDto expectedDto = new BookDto(
                bookId, "Clean Code", "Robert C. Martin", "1234567890123",
                BigDecimal.valueOf(29.99), "Desc", "url", Set.of(1L)
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actualDto = bookService.getBookById(bookId);

        assertThat(actualDto).isNotNull();
        assertThat(actualDto.getId()).isEqualTo(bookId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when book ID does not exist in getBookById")
    void getBookById_NonExistingId_ThrowsException() {
        Long bookId = 99L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find book by id: " + bookId);
    }

    @Test
    @DisplayName("Should update book successfully when ID exists")
    void updateBookById_ExistingId_ReturnsUpdatedBookDto() {
        Long bookId = 1L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto(
                "Updated Title", "Author", "1234567890123",
                BigDecimal.valueOf(39.99), "Desc", "url", Set.of(1L)
        );
        Book book = new Book();
        BookDto expectedDto = new BookDto(
                bookId, "Updated Title", "Author", "1234567890123",
                BigDecimal.valueOf(39.99), "Desc", "url", Set.of(1L)
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actualDto = bookService.updateBookById(bookId, requestDto);

        assertThat(actualDto).isNotNull();
        assertThat(actualDto.getTitle()).isEqualTo("Updated Title");
        verify(bookMapper).updateBookFromDto(requestDto, book);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existing book")
    void updateBookById_NonExistingId_ThrowsException() {
        Long bookId = 99L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBookById(bookId, requestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find book with id: " + bookId);
    }

    @Test
    @DisplayName("Should delete book successfully when ID exists")
    void deleteBookById_ExistingId_DeletesBook() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.deleteBookById(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existing book")
    void deleteBookById_NonExistingId_ThrowsException() {
        Long bookId = 99L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBookById(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find book with id: " + bookId);

        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should search books based on parameters")
    void search_ValidParams_ReturnsPageOfBooks() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(new String[]{"Clean"}, new String[]{"Martin"}, null);
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Book> spec = (root, query, cb) -> null;
        Book book = new Book();
        BookDto bookDto = new BookDto(1L, "Clean Code", "Robert C. Martin",
                "1234567890123", BigDecimal.valueOf(29.99), "Desc", "url", Set.of(1L));
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(specificationBuilder.build(params)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actualPage = bookService.search(params, pageable);

        assertThat(actualPage).isNotNull();
        assertThat(actualPage.getContent()).hasSize(1);
        assertThat(actualPage.getContent().get(0).getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @DisplayName("Should return books by category ID when category exists")
    void getBooksByCategoryId_ExistingCategory_ReturnsPage() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        BookDtoWithoutCategoryIds dtoWithoutCategories = new BookDtoWithoutCategoryIds();
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(bookRepository.findAllByCategoriesId(categoryId, pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dtoWithoutCategories);

        Page<BookDtoWithoutCategoryIds> actualPage =
                bookService.getBooksByCategoryId(categoryId, pageable);

        assertThat(actualPage).isNotNull();
        assertThat(actualPage.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when "
            + "category does not exist in getBooksByCategoryId")
    void getBooksByCategoryId_NonExistingCategory_ThrowsException() {
        Long categoryId = 99L;
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThatThrownBy(() -> bookService.getBooksByCategoryId(categoryId, pageable))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find category with id: " + categoryId);

        verify(bookRepository, never()).findAllByCategoriesId(any(), any());
    }
}
