package p.projects.springbookstore.dto;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {}
