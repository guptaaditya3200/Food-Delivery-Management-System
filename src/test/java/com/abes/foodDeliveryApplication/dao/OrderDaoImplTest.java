package com.abes.foodDeliveryApplication.dao;

import com.abes.foodDeliveryApplication.dto.Order;
import com.abes.foodDeliveryApplication.utils.CollectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class OrderDaoImplTest {
    
    private OrderDao orderDao;
    
    @BeforeEach
    void setUp() {
        CollectionUtil.clearAllData();
        orderDao = new OrderDaoImpl();
    }
    
    @Test
    @DisplayName("Should save order successfully")
    void testSaveOrder() {
        Map<String, Integer> items = new HashMap<>();
        items.put("Pizza", 2);
        Order order = new Order("ORD001", "CUST001", "DEL001", items, 25.98);
        
        orderDao.saveOrder(order);
        
        Order retrieved = orderDao.getOrderById("ORD001");
        assertNotNull(retrieved);
        assertEquals("ORD001", retrieved.getOrderId());
        assertEquals("CUST001", retrieved.getCustomerId());
    }
    
    @Test
    @DisplayName("Should get order by ID")
    void testGetOrderById() {
        Map<String, Integer> items = new HashMap<>();
        items.put("Burger", 1);
        Order order = new Order("ORD002", "CUST002", "DEL002", items, 8.99);
        orderDao.saveOrder(order);
        
        Order retrieved = orderDao.getOrderById("ORD002");
        
        assertNotNull(retrieved);
        assertEquals("ORD002", retrieved.getOrderId());
        assertEquals(8.99, retrieved.getTotalAmount());
    }
    
    @Test
    @DisplayName("Should return null for non-existent order")
    void testGetOrderByIdNotFound() {
        Order retrieved = orderDao.getOrderById("NONEXISTENT");
        assertNull(retrieved);
    }
    
    @Test
    @DisplayName("Should get orders by customer ID")
    void testGetOrdersByCustomerId() {
        Map<String, Integer> items1 = new HashMap<>();
        items1.put("Pizza", 1);
        Order order1 = new Order("ORD001", "CUST001", "DEL001", items1, 12.99);
        
        Map<String, Integer> items2 = new HashMap<>();
        items2.put("Burger", 2);
        Order order2 = new Order("ORD002", "CUST001", "DEL002", items2, 17.98);
        
        Map<String, Integer> items3 = new HashMap<>();
        items3.put("Pasta", 1);
        Order order3 = new Order("ORD003", "CUST002", "DEL001", items3, 9.99);
        
        orderDao.saveOrder(order1);
        orderDao.saveOrder(order2);
        orderDao.saveOrder(order3);
        
        List<Order> customerOrders = orderDao.getOrdersByCustomerId("CUST001");
        
        assertEquals(2, customerOrders.size());
        assertTrue(customerOrders.stream().allMatch(o -> o.getCustomerId().equals("CUST001")));
    }
    
    @Test
    @DisplayName("Should get orders by delivery person ID")
    void testGetOrdersByDeliveryPersonId() {
        Map<String, Integer> items1 = new HashMap<>();
        items1.put("Pizza", 1);
        Order order1 = new Order("ORD001", "CUST001", "DEL001", items1, 12.99);
        
        Map<String, Integer> items2 = new HashMap<>();
        items2.put("Burger", 1);
        Order order2 = new Order("ORD002", "CUST002", "DEL001", items2, 8.99);
        
        orderDao.saveOrder(order1);
        orderDao.saveOrder(order2);
        
        List<Order> deliveryOrders = orderDao.getOrdersByDeliveryPersonId("DEL001");
        
        assertEquals(2, deliveryOrders.size());
        assertTrue(deliveryOrders.stream().allMatch(o -> o.getDeliveryPersonId().equals("DEL001")));
    }
    
    @Test
    @DisplayName("Should update existing order")
    void testUpdateOrder() {
        Map<String, Integer> items = new HashMap<>();
        items.put("Pizza", 1);
        Order order = new Order("ORD001", "CUST001", "DEL001", items, 12.99);
        orderDao.saveOrder(order);
        
        order.setStatus("COMPLETED");
        orderDao.updateOrder(order);
        
        Order updated = orderDao.getOrderById("ORD001");
        assertEquals("COMPLETED", updated.getStatus());
    }
    
    @Test
    @DisplayName("Should get all orders")
    void testGetAllOrders() {
        Map<String, Integer> items1 = new HashMap<>();
        items1.put("Pizza", 1);
        Order order1 = new Order("ORD001", "CUST001", "DEL001", items1, 12.99);
        
        Map<String, Integer> items2 = new HashMap<>();
        items2.put("Burger", 1);
        Order order2 = new Order("ORD002", "CUST002", "DEL002", items2, 8.99);
        
        orderDao.saveOrder(order1);
        orderDao.saveOrder(order2);
        
        List<Order> allOrders = orderDao.getAllOrders();
        
        assertEquals(2, allOrders.size());
    }
}