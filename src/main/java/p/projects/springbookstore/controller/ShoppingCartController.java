package p.projects.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import p.projects.springbookstore.dto.AddCartItemRequestDto;
import p.projects.springbookstore.dto.ShoppingCartDto;
import p.projects.springbookstore.dto.UpdateCartItemRequestDto;
import p.projects.springbookstore.service.ShoppingCartService;

@Tag(name = "Shopping cart management", description = "Endpoints for managing user shopping cart")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get user shopping cart",
            description = "Returns the shopping cart for the currently authenticated user")
    public ShoppingCartDto getCart() {
        return shoppingCartService.getCartForCurrentUser();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Add item to shopping cart",
            description = "Adds a book with specified quantity to the user's shopping cart")
    public ShoppingCartDto addBookToCart(@RequestBody @Valid AddCartItemRequestDto requestDto) {
        return shoppingCartService.addBookToCart(requestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update cart item quantity",
            description = "Updates the quantity of a specific item in the user's shopping cart")
    public ShoppingCartDto updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateItemQuantity(cartItemId, requestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove item from shopping cart",
            description = "Removes a specific cart item from the user's shopping cart")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
