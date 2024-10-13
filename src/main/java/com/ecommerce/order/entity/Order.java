package com.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="order_details")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_id_seq")
    @SequenceGenerator(name="order_id_seq",initialValue = 1,allocationSize = 1)
    private Long id;;
    private Long productId;
    private Long quantity;
    private String orderStatus;
    private Timestamp orderDate;
    private Double amount;
}
