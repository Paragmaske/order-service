package com.ecommerce.order.entity.exception;

import lombok.Data;

@Data
public class ProductServiceCustomException  extends RuntimeException{
    private int errorCode;
  private Object object;
    public ProductServiceCustomException(String message,int errorCode,Object object)
    {
        super(message);
        this.errorCode=errorCode;
        this.object=object;
    }
}
