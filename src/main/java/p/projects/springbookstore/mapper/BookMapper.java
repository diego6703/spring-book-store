package p.projects.springbookstore.mapper;

import org.mapstruct.Mapper;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.BookDto;
import p.projects.springbookstore.dto.CreateBookRequestDto;
import p.projects.springbookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto requestDto);
}
