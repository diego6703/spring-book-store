package p.projects.springbookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.dto.UpdateBookRequestDto;
import p.projects.springbookstore.service.BookService;

@WebMvcTest(BookController.class)
public class BookControllerTest extends AbstractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should create book when user is ADMIN")
    void createBook_AsAdmin_ReturnsCreated() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Clean Code",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(29.99),
                "A Handbook of Agile Software Craftsmanship",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );

        BookDto responseDto = new BookDto(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(29.99),
                "A Handbook of Agile Software Craftsmanship",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );

        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.isbn").value("1234567890123"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should forbid book creation when user is regular USER")
    void createBook_AsUser_ReturnsForbidden() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Clean Code",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(29.99),
                "Description",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return paginated list of books for USER")
    void getAllBooks_AsUser_ReturnsPage() throws Exception {
        BookDto bookDto = new BookDto(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(29.99),
                "Description",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );
        PageImpl<BookDto> page = new PageImpl<>(java.util.List.of(bookDto),
                PageRequest.of(0, 20), 1);

        when(bookService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return book by ID for USER")
    void getBookById_AsUser_ReturnsBook() throws Exception {
        Long bookId = 1L;
        BookDto bookDto = new BookDto(
                bookId,
                "Clean Code",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(29.99),
                "Description",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );

        when(bookService.getBookById(bookId)).thenReturn(bookDto);

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should update book when user is ADMIN")
    void updateBook_AsAdmin_ReturnsUpdatedBook() throws Exception {
        Long bookId = 1L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto(
                "Clean Code Updated",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(39.99),
                "Updated Description",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );

        BookDto responseDto = new BookDto(
                bookId,
                "Clean Code Updated",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(39.99),
                "Updated Description",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );

        when(bookService.updateBookById(eq(bookId),
                any(UpdateBookRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should delete book and return NO_CONTENT when user is ADMIN")
    void deleteBook_AsAdmin_ReturnsNoContent() throws Exception {
        Long bookId = 1L;
        doNothing().when(bookService).deleteBookById(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should search books by parameters and return page")
    void searchBooks_AsUser_ReturnsPage() throws Exception {
        BookDto bookDto = new BookDto(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "1234567890123",
                BigDecimal.valueOf(29.99),
                "Description",
                "https://example.com/cover.jpg",
                Set.of(1L)
        );
        PageImpl<BookDto> page = new PageImpl<>(java.util.List.of(bookDto),
                PageRequest.of(0, 20), 1);

        when(bookService.search(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/books/search")
                        .param("title", "Clean Code")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should return 400 Bad Request when creating a book with invalid request body")
    void createBook_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateBookRequestDto invalidRequestDto = new CreateBookRequestDto();

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }
}
