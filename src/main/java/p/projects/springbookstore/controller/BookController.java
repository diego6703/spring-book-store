package p.projects.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.BookSearchParametersDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.dto.UpdateBookRequestDto;
import p.projects.springbookstore.service.BookService;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create a new book",
            description = "Creates a new book entity in the system")
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(requestDto));
    }

    @GetMapping
    @Operation(summary = "Get all books",
            description = "Returns a paginated list of all books")
    public ResponseEntity<Page<BookDto>> getAll(
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID",
            description = "Returns book details by its unique identifier")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updates an existing book record")
    public ResponseEntity<BookDto> updateBookById(@PathVariable Long id,
                                                  @Valid @RequestBody
                                                  UpdateBookRequestDto requestDto) {
        return ResponseEntity.ok(bookService.updateBookById(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Removes a book from the system")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by specific parameters")
    public ResponseEntity<Page<BookDto>> searchBooks(
            BookSearchParametersDto searchParameters,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(bookService.search(searchParameters, pageable));
    }
}
