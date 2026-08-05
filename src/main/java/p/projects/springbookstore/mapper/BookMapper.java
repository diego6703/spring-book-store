package p.projects.springbookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.BookDtoWithoutCategoryIds;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.dto.UpdateBookRequestDto;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.model.Category;
import p.projects.springbookstore.repository.CategoryRepository;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    @Mapping(target = "categories", source = "categoryIds")
    Book toEntity(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null && bookDto.getCategoryIds() != null) {
            bookDto.getCategoryIds().addAll(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", source = "categoryIds")
    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book book);

    default Set<Category> map(Set<Long> categoryIds) {
        if (categoryIds == null) {
            return null;
        }
        return categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}
