package p.projects.springbookstore.dto;

import jakarta.validation.constraints.NotNull;
import p.projects.springbookstore.model.Status;

public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Status cannot be null")
        Status status
) {}
