package com.omersoftware.cdc.postgresql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderEventListener {

  @KafkaListener(topics = "cdc.orderdb.order", groupId = "order-group")
  public void listen(String message) {
    log.info("Received CDC message: " + message);
    // Process the message
  }
}
