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
}
