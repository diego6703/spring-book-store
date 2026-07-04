package p.projects.springbookstore.repository.builder.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import p.projects.springbookstore.dto.BookSearchParametersDto;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.repository.builder.SpecificationBuilder;
import p.projects.springbookstore.repository.specification.SpecificationProvider;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final List<SpecificationProvider<Book>> providerList;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        List<Specification<Book>> specs = new ArrayList<>();
        if (searchParameters.author() != null) {
            specs.add(getProvider("author").getSpecification(searchParameters.author()));
        }
        if (searchParameters.title() != null) {
            specs.add(getProvider("title").getSpecification(searchParameters.title()));
        }
        if (searchParameters.isbn() != null) {
            specs.add(getProvider("isbn").getSpecification(searchParameters.isbn()));
        }
        if (specs.isEmpty()) {
            return null;
        }
        return Specification.allOf(specs);
    }

    private SpecificationProvider<Book> getProvider(String key) {
        return providerList.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No provider for key: " + key));
    }
}
