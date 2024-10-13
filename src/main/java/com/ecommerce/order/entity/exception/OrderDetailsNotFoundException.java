package com.ecommerce.order.entity.exception;


import lombok.Data;

@Data
public class OrderDetailsNotFoundException  extends RuntimeException{
    private String errorCode;

    public OrderDetailsNotFoundException(String message,String errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }
}
