package com.omersoftware.cdc.postgresql;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
  @Id @GeneratedValue private UUID id;

  private String customerName;

  private String customerEmail;

  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> customerAddress;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
