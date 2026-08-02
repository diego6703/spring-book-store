package p.projects.springbookstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.projects.springbookstore.dto.OrderDto;
import p.projects.springbookstore.dto.PlaceOrderRequestDto;
import p.projects.springbookstore.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderDto placeOrder(@RequestBody PlaceOrderRequestDto requestDto) {
        return orderService.placeOrder(requestDto);
    }
}
