package com.ecommerce.order.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Logging before the controller's placeOrder method
    @Before("execution(* com.ecommerce.order.controller.OrderController.placeOrder(..))")
    public void logBeforePlaceOrder(JoinPoint joinPoint) {
        logger.info("Entering placeOrder method with arguments: {}", joinPoint.getArgs());
    }

    // Logging after the controller's placeOrder method
    @AfterReturning(pointcut = "execution(* com.ecommerce.order.controller.OrderController.placeOrder(..))", returning = "result")
    public void logAfterPlaceOrder(JoinPoint joinPoint, Object result) {
        logger.info("Exiting placeOrder method with result: {}", result);
    }

    // Logging before the service's placeOrder method
    @Before("execution(* com.ecommerce.order.serviceImpl.OrderServiceImpl.placeOrder(..))")
    public void logBeforeServicePlaceOrder(JoinPoint joinPoint) {
        logger.info("Entering placeOrder in OrderServiceImpl with arguments: {}", joinPoint.getArgs());
    }

    // Logging after the service's placeOrder method
    @AfterReturning(pointcut = "execution(* com.ecommerce.order.serviceImpl.OrderServiceImpl.placeOrder(..))", returning = "result")
    public void logAfterServicePlaceOrder(JoinPoint joinPoint, Object result) {
        logger.info("Exiting placeOrder in OrderServiceImpl with result: {}", result);
    }

    // Logging before the controller's getOrderDetails method
    @Before("execution(* com.ecommerce.order.controller.OrderController.getOrderDetails(..))")
    public void logBeforeGetOrderDetails(JoinPoint joinPoint) {
        logger.info("Entering getOrderDetails method with arguments: {}", joinPoint.getArgs());
    }

    // Logging after the controller's getOrderDetails method
    @AfterReturning(pointcut = "execution(* com.ecommerce.order.controller.OrderController.getOrderDetails(..))", returning = "result")
    public void logAfterGetOrderDetails(JoinPoint joinPoint, Object result) {
        logger.info("Exiting getOrderDetails method with result: {}", result);
    }

    // Logging before the service's getOrderDetails method
    @Before("execution(* com.ecommerce.order.serviceImpl.OrderServiceImpl.getOrderDetails(..))")
    public void logBeforeServiceGetOrderDetails(JoinPoint joinPoint) {
        logger.info("Entering getOrderDetails in OrderServiceImpl with arguments: {}", joinPoint.getArgs());
    }

    // Logging after the service's getOrderDetails method
    @AfterReturning(pointcut = "execution(* com.ecommerce.order.serviceImpl.OrderServiceImpl.getOrderDetails(..))", returning = "result")
    public void logAfterServiceGetOrderDetails(JoinPoint joinPoint, Object result) {
        logger.info("Exiting getOrderDetails in OrderServiceImpl with result: {}", result);
    }
}
