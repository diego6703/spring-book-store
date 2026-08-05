package p.projects.springbookstore.service;

import p.projects.springbookstore.dto.AddCartItemRequestDto;
import p.projects.springbookstore.dto.ShoppingCartDto;
import p.projects.springbookstore.dto.UpdateCartItemRequestDto;
import p.projects.springbookstore.model.ShoppingCart;

public interface ShoppingCartService {

    ShoppingCartDto getCartForCurrentUser();

    ShoppingCartDto addBookToCart(AddCartItemRequestDto requestDto);

    void deleteCartItem(Long cartItemId);

    ShoppingCartDto updateItemQuantity(Long cartItemId, UpdateCartItemRequestDto requestDto);

    ShoppingCart getCartEntityForCurrentUser();

    void clearCart(ShoppingCart cart);
}
