package com.ecommerce.order.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;;
    private Long productId;
    private Long quantity;
    private String orderStatus;
    private Timestamp orderDate;
    private Double amount;
}
