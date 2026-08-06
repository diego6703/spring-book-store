package p.projects.springbookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.OrderItemDto;
import p.projects.springbookstore.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
