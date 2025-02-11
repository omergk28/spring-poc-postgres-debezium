package com.omersoftware.cdc.postgresql;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
@Validated
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public Order createOrder(@Valid @RequestBody OrderRequest request) {
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
    public void deleteOrder(@Valid @NotBlank UUID orderId) {
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
