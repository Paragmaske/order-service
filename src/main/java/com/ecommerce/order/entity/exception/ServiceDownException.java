package com.ecommerce.order.entity.exception;


import lombok.Data;

@Data
public class ServiceDownException  extends RuntimeException{
    private int errorCode;
    private Object object;
    public ServiceDownException(String message,int errorCode,Object object)
    {
        super(message);
        this.errorCode=errorCode;
        this.object=object;
    }
}
