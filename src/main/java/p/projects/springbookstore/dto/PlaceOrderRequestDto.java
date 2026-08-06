package p.projects.springbookstore.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequestDto(
        @NotBlank(message = "Shipping address cannot be blank")
        String shippingAddress
) {}
