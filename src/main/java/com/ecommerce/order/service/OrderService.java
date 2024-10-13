package com.ecommerce.order.service;

import com.ecommerce.order.model.OrderRequest;
import com.ecommerce.order.model.OrderResponse;
import com.ecommerce.order.model.ResponseModel;

public interface OrderService {
    ResponseModel placeOrder(OrderRequest orderRequest);

    ResponseModel getOrderDetails(long orderId);
}
