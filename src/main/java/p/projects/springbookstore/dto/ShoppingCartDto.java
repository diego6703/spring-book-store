package p.projects.springbookstore.dto;

import java.util.Set;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {}
