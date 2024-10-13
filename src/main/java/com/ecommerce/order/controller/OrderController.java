package com.ecommerce.order.controller;

import com.ecommerce.order.model.OrderRequest;
import com.ecommerce.order.model.OrderResponse;
import com.ecommerce.order.model.ResponseModel;
import com.ecommerce.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseModel> placeOrder(@RequestBody OrderRequest orderRequest) {
        logger.info("Received request to place order: {}", orderRequest);
        ResponseModel response = orderService.placeOrder(orderRequest);
        logger.info("Order placed successfully with response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getOrderDetails(@PathVariable("id") long orderId) {
        logger.info("Received request to get order details for orderId: {}", orderId);
        ResponseModel response = orderService.getOrderDetails(orderId);
        logger.info("Fetched order details successfully for orderId: {}", orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
