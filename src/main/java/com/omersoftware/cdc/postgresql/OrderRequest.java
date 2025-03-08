package com.omersoftware.cdc.postgresql;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
public class OrderRequest {

  @NotBlank private String customerName;

  @NotBlank private String customerEmail;

  private Map<String, Object> customerAddress;

  @Builder.Default private OrderStatus status = OrderStatus.PENDING;
}
