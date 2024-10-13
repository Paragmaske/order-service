package com.ecommerce.order.ControllerAdvice;

import com.ecommerce.order.entity.exception.OrderDetailsNotFoundException;
import com.ecommerce.order.entity.exception.ProductServiceCustomException;
import com.ecommerce.order.entity.exception.ServiceDownException;
import com.ecommerce.order.model.ResponseModel;
import com.ecommerce.order.model.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OrderDetailsNotFoundException.class)
    public ResponseEntity<ResponseModel> handleOrderDetailsNotFoundException(OrderDetailsNotFoundException ex) {
        logger.error("OrderDetailsNotFoundException: {}", ex.getMessage());

        return new ResponseEntity<>(ResponseModel.builder()
                .responseMsg(ex.getMessage())
                .statusModel(StatusModel.builder().statusCode(400).statusMsg("BAD REQUEST").build())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ResponseModel> handleProductServiceException(ProductServiceCustomException ex) {
        logger.error("ProductServiceCustomException: {}", ex.getMessage());

        return new ResponseEntity<>(ResponseModel.builder()
                .responseMsg(ex.getMessage())
                .statusModel(StatusModel.builder().statusCode(400).statusMsg("BAD REQUEST").build())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceDownException.class)
    public ResponseEntity<ResponseModel> handleServiceDownException(ServiceDownException ex) {
        logger.error("ServiceDownException: {}", ex.getMessage());

        return new ResponseEntity<>(ResponseModel.builder()
                .responseMsg(ex.getMessage())
                .statusModel(StatusModel.builder().statusCode(503).statusMsg("Service Unavailable").build())
                .build(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
