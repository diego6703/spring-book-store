package p.projects.springbookstore.repository.specification.impl;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.repository.specification.SpecificationProvider;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("isbns").in(Arrays.asList(params));
    }

    @Override
    public String getKey() {
        return "isbns";
    }
}
