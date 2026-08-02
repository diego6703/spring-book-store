package p.projects.springbookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.OrderDto;
import p.projects.springbookstore.model.Order;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);
}
