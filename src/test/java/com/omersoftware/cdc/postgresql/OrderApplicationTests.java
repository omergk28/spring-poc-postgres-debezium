package com.omersoftware.cdc.postgresql;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;

@SpringBootTest
class OrderApplicationTests {

  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:15")
          .withDatabaseName("orderdb")
          .withUsername("postgres")
          .withPassword("postgres");

  static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.kafka.consumer.bootstrap-servers", kafka::getBootstrapServers);
  }

  @BeforeAll
  static void beforeAll() {
    postgres.start();
    kafka.start();
  }

  @AfterAll
  static void afterAll() {
    kafka.stop();
    postgres.stop();
  }

  @Test
  void contextLoads() {}
}
