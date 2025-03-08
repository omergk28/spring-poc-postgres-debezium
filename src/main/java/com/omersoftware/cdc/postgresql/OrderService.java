package com.omersoftware.cdc.postgresql;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    return orderRepository.save(
        Order.builder()
            .customerName(request.getCustomerName())
            .customerEmail(request.getCustomerEmail())
            .customerAddress(request.getCustomerAddress())
            .status(request.getStatus())
            .build());
  }

  @GetMapping("/{orderId}")
  public Order getOrder(@PathVariable("orderId") UUID orderId) {
    return orderRepository
        .findById(orderId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
  }

  @PostMapping("/{orderId}")
  public Order updateOrder(
      @PathVariable("orderId") UUID orderId, @Valid @RequestBody OrderRequest request) {
    log.info("Updating order: {}", request);
    Order orderToUpdate = getOrder(orderId);
    BeanUtils.copyProperties(request, orderToUpdate);
    return orderRepository.save(orderToUpdate);
  }

  @DeleteMapping("/{orderId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrder(@PathVariable("orderId") UUID orderId) {
    log.info("Deleting order: {}", orderId);
    orderRepository.deleteById(orderId);
  }
}
