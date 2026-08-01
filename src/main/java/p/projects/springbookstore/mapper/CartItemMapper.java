package p.projects.springbookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.AddCartItemRequestDto;
import p.projects.springbookstore.dto.CartItemDto;
import p.projects.springbookstore.model.CartItem;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(AddCartItemRequestDto requestDto);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);
}
