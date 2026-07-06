package p.projects.springbookstore.repository.builder;

import org.springframework.data.jpa.domain.Specification;
import p.projects.springbookstore.dto.BookSearchParametersDto;
import p.projects.springbookstore.model.Book;

public interface BookSpecificationBuilder {
    Specification<Book> build(BookSearchParametersDto searchParameters);
}
