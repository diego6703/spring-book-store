package p.projects.springbookstore.repository.builder;

import org.springframework.data.jpa.domain.Specification;
import p.projects.springbookstore.dto.BookSearchParametersDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto searchParameters);
}
