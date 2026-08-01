package p.projects.springbookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import p.projects.springbookstore.config.MapperConfig;
import p.projects.springbookstore.dto.ShoppingCartDto;
import p.projects.springbookstore.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
