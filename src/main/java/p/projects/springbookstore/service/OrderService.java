package p.projects.springbookstore.service;

import java.util.List;
import p.projects.springbookstore.dto.OrderDto;
import p.projects.springbookstore.dto.OrderItemDto;
import p.projects.springbookstore.dto.PlaceOrderRequestDto;
import p.projects.springbookstore.dto.UpdateOrderStatusRequestDto;

public interface OrderService {
    OrderDto placeOrder(PlaceOrderRequestDto orderDto);

    List<OrderDto> getUserOrderHistory();

    OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
