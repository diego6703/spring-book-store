package p.projects.springbookstore.dto;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {}
