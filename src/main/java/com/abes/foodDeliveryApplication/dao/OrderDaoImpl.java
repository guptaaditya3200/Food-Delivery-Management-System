package com.abes.foodDeliveryApplication.dao;


import com.abes.foodDeliveryApplication.dto.Order;
import com.abes.foodDeliveryApplication.utils.CollectionUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDaoImpl implements OrderDao {
    
	@Override
    public void saveOrder(Order order) {
        CollectionUtil.addOrder(order);
    }
    
    @Override
    public Order getOrderById(String orderId) {
        return CollectionUtil.getOrderById(orderId);
    }
    
    @Override
    public List<Order> getOrdersByCustomerId(String customerId) {
        return CollectionUtil.getOrders().values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> getOrdersByDeliveryPersonId(String deliveryPersonId) {
        return CollectionUtil.getOrders().values().stream()
                .filter(order -> order.getDeliveryPersonId().equals(deliveryPersonId))
                .collect(Collectors.toList());
    }
    
    @Override
    public void updateOrder(Order order) {
        CollectionUtil.addOrder(order);
    }
    @Override
    public List<Order> getAllOrders() {
        Map<String, Order> orders = CollectionUtil.getOrders();
        return orders.values().stream().collect(Collectors.toList());
    }  
   
}
