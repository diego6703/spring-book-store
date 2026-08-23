package p.projects.springbookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import p.projects.springbookstore.service.impl.ShoppingCartServiceImpl;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private SecurityService securityService;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Captor
    private ArgumentCaptor<ShoppingCart> cartCaptor;

    @Captor
    private ArgumentCaptor<CartItem> cartItemCaptor;

    @Test
    @DisplayName("Should return shopping cart when cart exists for current user")
    void getCartForCurrentUser_CartExists_ReturnsCartDto() {
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, userId, Set.of());

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto actualDto = shoppingCartService.getCartForCurrentUser();

        assertThat(actualDto).isNotNull();
        assertThat(actualDto.userId()).isEqualTo(userId);
        verify(shoppingCartRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Should create and return empty cart when cart does not exist for current user")
    void getCartForCurrentUser_CartDoesNotExist_CreatesAndReturnsCartDto() {
        Long userId = 1L;
        User user = new User();
        ShoppingCart newCart = new ShoppingCart();
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, userId, Set.of());

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(securityService.getAuthenticatedUser()).thenReturn(user);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(newCart);
        when(shoppingCartMapper.toDto(newCart)).thenReturn(expectedDto);

        ShoppingCartDto actualDto = shoppingCartService.getCartForCurrentUser();

        assertThat(actualDto).isNotNull();

        verify(shoppingCartRepository).save(cartCaptor.capture());
        assertThat(cartCaptor.getValue().getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should add new item to cart when item does not exist in cart yet")
    void addBookToCart_NewItem_AddsAndReturnsCart() {
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        Book book = new Book();
        CartItem cartItem = new CartItem();
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, userId, Set.of());
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto(10L, 2);

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByShoppingCartIdAndBookId(1L, 10L))
                .thenReturn(Optional.empty());
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(cartItemMapper.toEntity(requestDto)).thenReturn(cartItem);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto actualDto = shoppingCartService.addBookToCart(requestDto);

        assertThat(actualDto).isNotNull();

        verify(cartItemRepository).save(cartItemCaptor.capture());

        CartItem savedItem = cartItemCaptor.getValue();
        assertThat(savedItem.getShoppingCart()).isEqualTo(cart);
        assertThat(savedItem.getBook()).isEqualTo(book);
    }

    @Test
    @DisplayName("Should increment quantity when adding book that already exists in cart")
    void addBookToCart_ExistingItem_IncrementsQuantityAndReturnsCart() {
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);

        CartItem existingItem = new CartItem();
        existingItem.setQuantity(3);

        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, userId, Set.of());
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto(10L, 2);

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByShoppingCartIdAndBookId(1L, 10L))
                .thenReturn(Optional.of(existingItem));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto actualDto = shoppingCartService.addBookToCart(requestDto);

        assertThat(actualDto).isNotNull();
        assertThat(existingItem.getQuantity()).isEqualTo(5);
        verify(cartItemRepository).save(existingItem);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when book to add does not exist")
    void addBookToCart_BookNotFound_ThrowsException() {
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto(99L, 2);

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByShoppingCartIdAndBookId(1L, 99L))
                .thenReturn(Optional.empty());
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingCartService.addBookToCart(requestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    @DisplayName("Should update item quantity successfully when cart item exists")
    void updateItemQuantity_ExistingCartItem_ReturnsUpdatedCart() {
        Long userId = 1L;
        Long cartItemId = 5L;
        int expectedQuantity = 4;

        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(expectedQuantity);
        CartItem cartItem = new CartItem();
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, userId, Set.of());

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(cartItemRepository.findByIdAndShoppingCartUserId(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto actualDto = shoppingCartService.updateItemQuantity(cartItemId, requestDto);

        assertThat(actualDto).isNotNull();
        assertThat(cartItem.getQuantity()).isEqualTo(expectedQuantity);

        verify(cartItemRepository).save(cartItemCaptor.capture());

        CartItem savedItem = cartItemCaptor.getValue();
        assertThat(savedItem.getShoppingCart()).isEqualTo(cart);
        assertThat(savedItem.getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existing cart item")
    void updateItemQuantity_NonExistingCartItem_ThrowsException() {
        Long userId = 1L;
        Long cartItemId = 99L;
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(4);

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(cartItemRepository.findByIdAndShoppingCartUserId(cartItemId, userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingCartService.updateItemQuantity(cartItemId, requestDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Cart item not found with id: " + cartItemId + " for current user");
    }

    @Test
    @DisplayName("Should delete cart item successfully when item exists")
    void deleteCartItem_ExistingItem_DeletesCartItem() {
        Long userId = 1L;
        Long cartItemId = 5L;
        CartItem cartItem = new CartItem();

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(cartItemRepository.findByIdAndShoppingCartUserId(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));

        shoppingCartService.deleteCartItem(cartItemId);

        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existing cart item")
    void deleteCartItem_NonExistingItem_ThrowsException() {
        Long userId = 1L;
        Long cartItemId = 99L;

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(cartItemRepository.findByIdAndShoppingCartUserId(cartItemId, userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingCartService.deleteCartItem(cartItemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Cart item not found with id: " + cartItemId + " for current user");
    }

    @Test
    @DisplayName("Should return shopping cart entity when cart exists for current user")
    void getCartEntityForCurrentUser_CartExists_ReturnsCartEntity() {
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        ShoppingCart actualCart = shoppingCartService.getCartEntityForCurrentUser();

        assertThat(actualCart).isNotNull();
        assertThat(actualCart.getId()).isEqualTo(1L);
        verify(shoppingCartRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Should create and return empty shopping cart entity when cart does not exist")
    void getCartEntityForCurrentUser_CartDoesNotExist_CreatesAndReturnsCartEntity() {
        Long userId = 1L;
        ShoppingCart newCart = new ShoppingCart();
        newCart.setId(1L);
        User user = new User();

        when(securityService.getAuthenticatedUserId()).thenReturn(userId);
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(securityService.getAuthenticatedUser()).thenReturn(user);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(newCart);

        ShoppingCart actualCart = shoppingCartService.getCartEntityForCurrentUser();

        assertThat(actualCart).isNotNull();

        verify(shoppingCartRepository).save(cartCaptor.capture());
        assertThat(cartCaptor.getValue().getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should clear all items from shopping cart and save it")
    void clearCart_ValidCart_ClearsItemsAndSaves() {
        ShoppingCart cart = new ShoppingCart();
        CartItem item = new CartItem();
        cart.getCartItems().add(item);

        shoppingCartService.clearCart(cart);

        assertThat(cart.getCartItems()).isEmpty();
        verify(shoppingCartRepository).save(cart);
    }
}
