package com.ecommerce.order.serviceImpl;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.ProductResponse;
import com.ecommerce.order.entity.exception.ProductServiceCustomException;
import com.ecommerce.order.entity.exception.ServiceDownException;
import com.ecommerce.order.model.OrderRequest;
import com.ecommerce.order.model.PaymentRequest;
import com.ecommerce.order.model.ResponseModel;
import com.ecommerce.order.model.StatusModel;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final WebClient productWebclient;
    private final WebClient paymentWebClient;

    @Autowired
    public OrderServiceImpl(WebClient.Builder webClientBuilder, OrderRepository orderRepository) {
        this.productWebclient = webClientBuilder.baseUrl("http://product-app:8080/product").build();
        this.paymentWebClient = webClientBuilder.baseUrl("http://payment-app:8082/payment").build();
        this.orderRepository = orderRepository;
    }


    @Override
    public ResponseModel placeOrder(OrderRequest orderRequest) {
        logger.info("Placing order with request: {}", orderRequest);
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(new Timestamp(System.currentTimeMillis()))
                .quantity(orderRequest.getQuantity())
                .build();

        // Attempt to reduce product quantity
        try {
             productWebclient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/reduceQuantity/{id}")
                            .queryParam("quantity", orderRequest.getQuantity())
                            .build(orderRequest.getProductId()))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.bodyToMono(ResponseModel.class)
                                    .flatMap(errorResponse -> {
                                        logger.error("Product service error: {}", errorResponse.getResponseMsg());
                                        return Mono.error(new ProductServiceCustomException(
                                                errorResponse.getResponseMsg(), 400, orderRequest));
                                    })
                    )
                    .bodyToMono(ResponseModel.class)
                    .block();

            logger.info("Product quantity reduced successfully, saving order: {}", order);
            Order savedOrder = orderRepository.save(order);

            // Create payment request and attempt payment
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .orderId(savedOrder.getId())
                    .amount(savedOrder.getAmount())
                    .referenceNumber(UUID.randomUUID().toString())
                    .paymentMode(orderRequest.getPayMentMode())
                    .build();

            try {
                Long paymentId = paymentWebClient.post()
                        .uri("") // Specify the payment endpoint here
                        .bodyValue(paymentRequest)
                        .retrieve()
                        .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                                Mono.error(new ServiceDownException("Payment service is down", 500, orderRequest))
                        )
                        .bodyToMono(Long.class)
                        .block();

                logger.info("Payment successful with paymentId: {}", paymentId);
                markOrderAsFailed(savedOrder, "SUCCESSFUL");

                return ResponseModel.builder()
                        .statusModel(StatusModel.builder().statusCode(200).statusMsg("SUCCESS").build())
                        .responseMsg("ORDER_PLACED")
                        .responseModel(savedOrder)
                        .build();

            } catch (WebClientRequestException e) {
                markOrderAsFailed(order, "FAILED");
                logger.error("Payment service down. Rolling back product quantity reduction.");

                // Call the revert API to roll back the quantity in case of payment failure
                try {
                    productWebclient.put()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/revertQuantity/{id}")
                                    .queryParam("quantity", orderRequest.getQuantity())
                                    .build(orderRequest.getProductId()))
                            .retrieve()
                            .bodyToMono(ProductResponse.class)
                            .block();
                    logger.info("Product quantity reverted successfully for productId: {}", orderRequest.getProductId());
                } catch (Exception rollbackException) {
                    logger.error("Failed to revert product quantity: {}", rollbackException.getMessage());
                    throw new ServiceDownException("Payment service is down and rollback failed", 500, orderRequest);
                }

                throw new ServiceDownException("Payment service is down", 500, orderRequest);
            }

        } catch (ProductServiceCustomException e) {
            markOrderAsFailed(order, "FAILED");
            logger.error("Product service custom exception: {}", e.getMessage());
            throw e;
        } catch (WebClientRequestException e) {
            markOrderAsFailed(order, "FAILED");
            logger.error("Product service down exception: {}", e.getMessage());
            throw new ServiceDownException("Product service is down", 500, orderRequest);
        }
    }

    private void markOrderAsFailed(Order order, String FAILED) {
        order.setOrderStatus(FAILED);
        orderRepository.save(order);
    }

    @Override
    public ResponseModel getOrderDetails(long orderId) {
        logger.info("Fetching order details for orderId: {}", orderId);
        Order order = checkIfOrderExists(orderId);
        logger.info("Order found: {}", order);
        return ResponseModel.builder()
                .statusModel(StatusModel.builder().statusCode(200).statusMsg("SUCCESS").build())
                .responseMsg("ORDER_FOUND")
                .responseModel(order)
                .build();
    }

    private Order checkIfOrderExists(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    String errorMessage = "Order with id " + orderId + " not found";
                    logger.warn("Order not found: {}", errorMessage);
                    return new ProductServiceCustomException(errorMessage, 400, "NOT_FOUND");
                });
    }
}
