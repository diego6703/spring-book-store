package p.projects.springbookstore.repository.builder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import p.projects.springbookstore.dto.BookSearchParametersDto;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.repository.builder.BookSpecificationBuilder;
import p.projects.springbookstore.repository.specification.SpecificationProvider;

@Component
public class BookSpecificationBuilderImpl implements BookSpecificationBuilder {
    private final Map<String, SpecificationProvider<Book>> providerMap;

    public BookSpecificationBuilderImpl(List<SpecificationProvider<Book>> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(SpecificationProvider::getKey, p -> p));
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        List<Specification<Book>> specs = new ArrayList<>();

        addSpecificationIfValid(specs, "titles", searchParameters.titles());
        addSpecificationIfValid(specs, "authors", searchParameters.authors());
        addSpecificationIfValid(specs, "isbns", searchParameters.isbns());

        return specs.stream()
                .reduce(Specification::and)
                .orElse((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
    }

    private SpecificationProvider<Book> getProvider(String key) {
        SpecificationProvider<Book> provider = providerMap.get(key);
        if (provider == null) {
            throw new IllegalArgumentException("No provider for key: " + key);
        }
        return provider;
    }

    private void addSpecificationIfValid(
            List<Specification<Book>> specs, String key, String[] params) {
        if (params != null && params.length > 0) {
            String[] filteredParams = Arrays.stream(params)
                    .filter(p -> p != null && !p.isBlank())
                    .toArray(String[]::new);

            if (filteredParams.length > 0) {
                specs.add(getProvider(key).getSpecification(filteredParams));
            }
        }
    }
}
