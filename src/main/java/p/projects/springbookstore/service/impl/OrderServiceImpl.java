package p.projects.springbookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import p.projects.springbookstore.dto.OrderDto;
import p.projects.springbookstore.dto.OrderItemDto;
import p.projects.springbookstore.dto.PlaceOrderRequestDto;
import p.projects.springbookstore.dto.UpdateOrderStatusRequestDto;
import p.projects.springbookstore.exception.EntityNotFoundException;
import p.projects.springbookstore.mapper.OrderItemMapper;
import p.projects.springbookstore.mapper.OrderMapper;
import p.projects.springbookstore.model.CartItem;
import p.projects.springbookstore.model.Order;
import p.projects.springbookstore.model.OrderItem;
import p.projects.springbookstore.model.ShoppingCart;
import p.projects.springbookstore.model.Status;
import p.projects.springbookstore.repository.OrderItemRepository;
import p.projects.springbookstore.repository.OrderRepository;
import p.projects.springbookstore.security.SecurityService;
import p.projects.springbookstore.service.OrderService;
import p.projects.springbookstore.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final SecurityService securityService;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(PlaceOrderRequestDto orderDto) {
        ShoppingCart cartForCurrentUser = shoppingCartService.getCartEntityForCurrentUser();

        if (cartForCurrentUser.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }
        Order order = new Order();
        order.setUser(cartForCurrentUser.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setShippingAddress(orderDto.shippingAddress());

        Set<OrderItem> orderItems = cartForCurrentUser.getCartItems().stream()
                .map(cartItem -> mapToOrderItem(cartItem, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        shoppingCartService.clearCart(cartForCurrentUser);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrderHistory() {
        Long userId = securityService.getAuthenticatedUser().getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);

        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));

        order.setStatus(requestDto.status());
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> getOrderItems(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found with id: " + orderId);
        }

        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));

        return orderItemMapper.toDto(orderItem);
    }

    private OrderItem mapToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItem;
    }

}
