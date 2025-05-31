package com.abes.foodDeliveryApplication.services;



import com.abes.foodDeliveryApplication.dto.Order;
import com.abes.foodDeliveryApplication.exception.InvalidOrderException;
import java.util.Map;
import java.util.List;

public interface OrderService {
    Order placeOrder(String customerId, Map<String, Integer> requestedItems)
            throws InvalidOrderException;

    void completeOrder(String orderId) throws InvalidOrderException;

    Order getOrderById(String orderId);

    List<Order> getOrdersByCustomerId(String customerId);

    List<Order> getOrdersByDeliveryPersonId(String deliveryPersonId);

    String getOrderDetails(String orderId);
}
