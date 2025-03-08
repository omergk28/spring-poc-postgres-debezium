package com.omersoftware.cdc.postgresql;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

  @Valid private Address customerAddress;

  @Builder.Default private OrderStatus status = OrderStatus.PENDING;
}
