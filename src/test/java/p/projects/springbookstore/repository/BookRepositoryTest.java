package p.projects.springbookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find all books by category id should return page with one book")
    void findAllByCategoriesId_ShouldReturnBookPage() {
        // Given
        Category category = new Category();
        category.setName("Programming");
        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Spring Boot in Action");
        book.setAuthor("Craig Walls");
        book.setIsbn("9781617292547");
        book.setPrice(BigDecimal.valueOf(99.00));
        book.setCategories(Set.of(savedCategory));

        bookRepository.save(book);

        Page<Book> actualPage = bookRepository.findAllByCategoriesId(
                savedCategory.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(actualPage).isNotNull();
        assertThat(actualPage.getContent()).hasSize(1);
        assertThat(actualPage.getContent().get(0).getTitle()).isEqualTo("Spring Boot in Action");
    }
}
