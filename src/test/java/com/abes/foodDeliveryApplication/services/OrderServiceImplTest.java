package com.abes.foodDeliveryApplication.services;

import com.abes.foodDeliveryApplication.dto.Order;
import com.abes.foodDeliveryApplication.exception.InvalidOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@DisplayName("OrderService Tests")
class OrderServiceImplTest {
    
    private OrderService orderService;
    private UserService userService;
    private FoodService foodService;
    
    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl();
        userService = new UserServiceImpl();
        foodService = new FoodServiceImpl();
        
        // Set up test data
        setupTestData();
    }
    
    private void setupTestData() {
        // Register test users
        userService.registerCustomer("CUST001", "Test Customer", "customer@example.com", "1234567890", "password");
        userService.registerDeliveryPerson("DEL001", "Test Delivery", "delivery@example.com", "0987654321", "password");
        
        // Add test food items
        foodService.addNewFoodItem("Pizza", 12.99, 10);
        foodService.addNewFoodItem("Burger", 8.99, 5);
        foodService.addNewFoodItem("Fries", 3.99, 15);
    }
    
    @Nested
    @DisplayName("Place Order Tests")
    class PlaceOrderTests {
        
        @Test
        @DisplayName("Should place order successfully with valid data")
        void shouldPlaceOrderSuccessfullyWithValidData() {
            // Given
            String customerId = "CUST001";
            Map<String, Integer> requestedItems = new HashMap<>();
            requestedItems.put("Pizza", 2);
            requestedItems.put("Fries", 1);
            
            // When & Then
            assertDoesNotThrow(() -> {
                Order order = orderService.placeOrder(customerId, requestedItems);
                
                assertNotNull(order);
                assertNotNull(order.getOrderId());
                assertEquals(customerId, order.getCustomerId());
                assertNotNull(order.getDeliveryPersonId());
                assertEquals("PLACED", order.getStatus());
                assertEquals(29.97, order.getTotalAmount(), 0.01); // 2*12.99 + 1*3.99
            });
        }
        
        @Test
        @DisplayName("Should throw exception for non-existent customer")
        void shouldThrowExceptionForNonExistentCustomer() {
            // Given
            String invalidCustomerId = "INVALID_CUSTOMER";
            Map<String, Integer> requestedItems = new HashMap<>();
            requestedItems.put("Pizza", 1);
            
            // When & Then
            assertThrows(InvalidOrderException.class, 
                () -> orderService.placeOrder(invalidCustomerId, requestedItems));
        }
        
        @Test
        @DisplayName("Should throw exception for non-existent food item")
        void shouldThrowExceptionForNonExistentFoodItem() {
            // Given
            String customerId = "CUST001";
            Map<String, Integer> requestedItems = new HashMap<>();
            requestedItems.put("NonExistentItem", 1);
            
            // When & Then
            assertThrows(InvalidOrderException.class, 
                () -> orderService.placeOrder(customerId, requestedItems));
        }
        
        @Test
        @DisplayName("Should throw exception when no delivery person available")
        void shouldThrowExceptionWhenNoDeliveryPersonAvailable() {
            // Given
            String customerId = "CUST001";
            Map<String, Integer> requestedItems = new HashMap<>();
            requestedItems.put("Pizza", 1);
            
            // Make delivery person unavailable
            assertDoesNotThrow(() -> userService.updateUserAvailability("DEL001", false));
            
            // When & Then
            assertThrows(InvalidOrderException.class, 
                () -> orderService.placeOrder(customerId, requestedItems));
        }
    }
    
    @Nested
    @DisplayName("Complete Order Tests")
    class CompleteOrderTests {
        
        @Test
        @DisplayName("Should complete order successfully")
        void shouldCompleteOrderSuccessfully() {
            // Given
            String customerId = "CUST001";
            Map<String, Integer> requestedItems = new HashMap<>();
            requestedItems.put("Pizza", 1);
            
            assertDoesNotThrow(() -> {
                Order order = orderService.placeOrder(customerId, requestedItems);
                String orderId = order.getOrderId();
                
                // When
                orderService.completeOrder(orderId);
                
                // Then
                Order completedOrder = orderService.getOrderById(orderId);
                assertEquals("COMPLETED", completedOrder.getStatus());
            });
        }
        
        @Test
        @DisplayName("Should throw exception for non-existent order")
        void shouldThrowExceptionForNonExistentOrder() {
            // When & Then
            assertThrows(InvalidOrderException.class, 
                () -> orderService.completeOrder("INVALID_ORDER_ID"));
        }
    }
    
    @Nested
    @DisplayName("Get Order Tests")
    class GetOrderTests {
        
        @Test
        @DisplayName("Should get order by ID")
        void shouldGetOrderById() {
            // Given
            String customerId = "CUST001";
            Map<String, Integer> requestedItems = new HashMap<>();
            requestedItems.put("Burger", 1);
            
            assertDoesNotThrow(() -> {
                Order placedOrder = orderService.placeOrder(customerId, requestedItems);
                String orderId = placedOrder.getOrderId();
                
                // When
                Order retrievedOrder = orderService.getOrderById(orderId);
                
                // Then
                assertNotNull(retrievedOrder);
                assertEquals(orderId, retrievedOrder.getOrderId());
                assertEquals(customerId, retrievedOrder.getCustomerId());
            });
        }
        
        @Test
        @DisplayName("Should get orders by delivery person ID")
        void shouldGetOrdersByDeliveryPersonId() {
            // Given
            String customerId = "CUST001";
            Map<String, Integer> requestedItems = new HashMap<>();
            requestedItems.put("Fries", 2);
            
            assertDoesNotThrow(() -> {
                Order order = orderService.placeOrder(customerId, requestedItems);
                String deliveryPersonId = order.getDeliveryPersonId();
                
                // When
                List<Order> deliveryOrders = orderService.getOrdersByDeliveryPersonId(deliveryPersonId);
                
                // Then
                assertNotNull(deliveryOrders);
                assertFalse(deliveryOrders.isEmpty());
                assertTrue(deliveryOrders.stream().allMatch(o -> o.getDeliveryPersonId().equals(deliveryPersonId)));
            });
        }
    }
    
    @Nested
    @DisplayName("Get Order Details Tests")
    class GetOrderDetailsTests {
        
        @Test
        @DisplayName("Should return 'Order not found' for invalid order ID")
        void shouldReturnOrderNotFoundForInvalidOrderId() {
            // When
            String orderDetails = orderService.getOrderDetails("INVALID_ORDER_ID");
            
            // Then
            assertEquals("Order not found", orderDetails);
        }
    }
}