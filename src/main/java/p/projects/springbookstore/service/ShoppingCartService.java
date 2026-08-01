package p.projects.springbookstore.service;

import p.projects.springbookstore.dto.AddCartItemRequestDto;
import p.projects.springbookstore.dto.ShoppingCartDto;
import p.projects.springbookstore.dto.UpdateCartItemRequestDto;

public interface ShoppingCartService {

    ShoppingCartDto getCartForCurrentUser();

    ShoppingCartDto addBookToCart(AddCartItemRequestDto requestDto);

    void deleteCartItem(Long cartItemId);

    ShoppingCartDto updateItemQuantity(Long cartItemId, UpdateCartItemRequestDto requestDto);
}
