package p.projects.springbookstore.service;

import java.util.List;
import p.projects.springbookstore.dto.OrderDto;
import p.projects.springbookstore.dto.PlaceOrderRequestDto;

public interface OrderService {
    OrderDto placeOrder(PlaceOrderRequestDto orderDto);

    List<OrderDto> getUserOrderHistory();
}
