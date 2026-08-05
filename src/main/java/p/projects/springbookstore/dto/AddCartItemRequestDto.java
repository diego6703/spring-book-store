package p.projects.springbookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequestDto(
        @NotNull(message = "ID cannot be blank")
        Long bookId,

        @NotNull(message = "Quantity cannot be blank")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {}
