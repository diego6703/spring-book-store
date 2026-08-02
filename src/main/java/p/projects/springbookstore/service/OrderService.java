package p.projects.springbookstore.service;

import p.projects.springbookstore.dto.OrderDto;
import p.projects.springbookstore.dto.PlaceOrderRequestDto;

public interface OrderService {
    OrderDto placeOrder(PlaceOrderRequestDto orderDto);
}
