package p.projects.springbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import p.projects.springbookstore.dto.AddCartItemRequestDto;
import p.projects.springbookstore.dto.ShoppingCartDto;
import p.projects.springbookstore.dto.UpdateCartItemRequestDto;
import p.projects.springbookstore.exception.EntityNotFoundException;
import p.projects.springbookstore.mapper.CartItemMapper;
import p.projects.springbookstore.mapper.ShoppingCartMapper;
import p.projects.springbookstore.model.Book;
import p.projects.springbookstore.model.CartItem;
import p.projects.springbookstore.model.ShoppingCart;
import p.projects.springbookstore.model.User;
import p.projects.springbookstore.repository.BookRepository;
import p.projects.springbookstore.repository.CartItemRepository;
import p.projects.springbookstore.repository.ShoppingCartRepository;
import p.projects.springbookstore.security.SecurityService;
import p.projects.springbookstore.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final SecurityService securityService;
    private final BookRepository bookRepository;


    @Override
    public ShoppingCartDto getCartForCurrentUser() {
        Long userId = securityService.getAuthenticatedUserId();

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));

        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCart getCartEntityForCurrentUser() {
        Long userId = securityService.getAuthenticatedUserId();
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToCart(AddCartItemRequestDto requestDto) {
        Long userId = securityService.getAuthenticatedUserId();

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));

        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(
                cart.getId(), requestDto.bookId())
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + requestDto.quantity());
                    return existingItem;
                })
                .orElseGet(() -> {
                    Book book = bookRepository.findById(requestDto.bookId())
                            .orElseThrow(() -> new EntityNotFoundException("Book not found"));

                    CartItem newItem = cartItemMapper.toEntity(requestDto);
                    newItem.setShoppingCart(cart);
                    newItem.setBook(book);
                    return newItem;
                });

        cartItemRepository.save(cartItem);
        return getCartForCurrentUser();
    }

    @Override
    @Transactional
    public ShoppingCartDto updateItemQuantity(Long cartItemId,
                                              UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id: " + cartItemId));

        cartItem.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItem);

        return getCartForCurrentUser();
    }

    private ShoppingCart createEmptyCart(Long userId) {
        User user = securityService.getAuthenticatedUser();
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(user);
        return shoppingCartRepository.save(newCart);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id: " + cartItemId));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(ShoppingCart cart) {
        cart.getCartItems().clear();
        shoppingCartRepository.save(cart);
    }
}
