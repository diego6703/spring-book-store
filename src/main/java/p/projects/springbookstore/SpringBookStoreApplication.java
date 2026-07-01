package p.projects.springbookstore;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.service.BookService;

@SpringBootApplication
public class SpringBookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("The Great Gatsby");
            book.setAuthor("F. Scott Fitzgerald");
            book.setIsbn("1234567890");
            book.setPrice(new BigDecimal("19.99"));

            bookService.save(book);
            System.out.println("Saved book: " + book.getTitle());

            List<Book> allBooks = bookService.findAll();
            System.out.println("Books in database: " + allBooks.size());
        };
    }
}
