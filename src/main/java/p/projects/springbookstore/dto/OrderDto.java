package p.projects.springbookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import p.projects.springbookstore.model.Status;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Status status
) {}

