package p.projects.springbookstore.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import p.projects.springbookstore.dto.OrderDto;
import p.projects.springbookstore.dto.OrderItemDto;
import p.projects.springbookstore.dto.PlaceOrderRequestDto;
import p.projects.springbookstore.dto.UpdateOrderStatusRequestDto;
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

    @GetMapping
    public List<OrderDto> getOrderHistory() {
        return orderService.getUserOrderHistory();
    }

    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @GetMapping("/{id}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long id) {
        return orderService.getOrderItems(id);
    }
}
