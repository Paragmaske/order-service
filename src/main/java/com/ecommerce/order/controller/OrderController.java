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


    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseModel> placeOrder(@RequestBody OrderRequest orderRequest) {
        ResponseModel response = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getOrderDetails(@PathVariable("id") long orderId) {
        ResponseModel response = orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
