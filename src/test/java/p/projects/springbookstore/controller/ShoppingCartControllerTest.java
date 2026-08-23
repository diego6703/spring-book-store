package p.projects.springbookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import p.projects.springbookstore.dto.AddCartItemRequestDto;
import p.projects.springbookstore.dto.CartItemDto;
import p.projects.springbookstore.dto.ShoppingCartDto;
import p.projects.springbookstore.dto.UpdateCartItemRequestDto;
import p.projects.springbookstore.service.ShoppingCartService;

@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest extends AbstractControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShoppingCartService shoppingCartService;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return shopping cart when user is authenticated")
    void getCart_AsUser_ReturnsCart() throws Exception {
        ShoppingCartDto cartDto = new ShoppingCartDto(
                1L,
                1L,
                Set.of(new CartItemDto(1L, 1L, "Clean Code", 2))
        );

        when(shoppingCartService.getCartForCurrentUser()).thenReturn(cartDto);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.cartItems").isArray())
                .andExpect(jsonPath("$.cartItems[0].bookTitle").value("Clean Code"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should add item to shopping cart and return created cart")
    void addBookToCart_AsUser_ReturnsCreatedCart() throws Exception {
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto(1L, 2);
        ShoppingCartDto responseDto = new ShoppingCartDto(
                1L,
                1L,
                Set.of(new CartItemDto(1L, 1L, "Clean Code", 2))
        );

        when(shoppingCartService.addBookToCart(any(AddCartItemRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/cart")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should update cart item quantity and return updated cart")
    void updateItemQuantity_AsUser_ReturnsUpdatedCart() throws Exception {
        Long cartItemId = 1L;
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(5);
        ShoppingCartDto responseDto = new ShoppingCartDto(
                1L,
                1L,
                Set.of(new CartItemDto(cartItemId, 1L, "Clean Code", 5))
        );

        when(shoppingCartService.updateItemQuantity(eq(cartItemId),
                any(UpdateCartItemRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/cart/cart-items/{cartItemId}", cartItemId)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems[0].quantity").value(5));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should delete cart item and return NO_CONTENT")
    void deleteCartItem_AsUser_ReturnsNoContent() throws Exception {
        Long cartItemId = 1L;
        doNothing().when(shoppingCartService).deleteCartItem(cartItemId);

        mockMvc.perform(delete("/api/cart/cart-items/{cartItemId}", cartItemId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 Bad Request when adding item with invalid request body")
    void addBookToCart_InvalidRequest_ReturnsBadRequest() throws Exception {
        AddCartItemRequestDto invalidRequestDto = new AddCartItemRequestDto(null, null);

        mockMvc.perform(post("/api/cart")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should allow ADMIN to get shopping cart")
    void getCart_AsAdmin_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk());
    }
}
