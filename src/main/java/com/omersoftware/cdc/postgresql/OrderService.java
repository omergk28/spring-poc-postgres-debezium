package com.omersoftware.cdc.postgresql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest request) {
        log.info("Creating order: {}", request);
        return orderRepository.save(Order.builder()
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerAddress(request.getCustomerAddress())
                .status(request.getStatus())
                .build());
    }

    @GetMapping("/{orderId}")
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RestClientResponseException("Order not found", 404, "Order not found", null, null, null));
    }

    @PostMapping("/{orderId}")
    public Order updateOrder(UUID orderId, Order order) {
        Order existingOrder = getOrder(orderId);
        existingOrder.setStatus(order.getStatus());
        return orderRepository.save(existingOrder);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findAllByStatus(status);
    }

}
