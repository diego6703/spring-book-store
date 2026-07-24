package p.projects.springbookstore.mapper;

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

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.getCategoryIds().addAll(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book book);
}
