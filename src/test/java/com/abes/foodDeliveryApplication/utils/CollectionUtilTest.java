package com.abes.foodDeliveryApplication.utils;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.dto.FoodItem;
import com.abes.foodDeliveryApplication.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

class CollectionUtilTest {
    
    @BeforeEach
    void setUp() {
        CollectionUtil.clearAllData();
    }
    
    @Test
    @DisplayName("Should have default manager after initialization")
    void testDefaultManagerExists() {
        User manager = CollectionUtil.getUserById("MGR001");
        
        assertNotNull(manager);
        assertEquals("System Manager", manager.getName());
        assertEquals("MANAGER", manager.getRole());
        assertEquals("admin123", manager.getPassword());
    }
    
    @Test
    @DisplayName("Should add user to appropriate collections")
    void testAddUser() {
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        User delivery = new User("DEL001", "Jane Smith", "jane@example.com", "9876543210", "password456", "DELIVERY");
        
        CollectionUtil.addUser(customer);
        CollectionUtil.addUser(delivery);
        
        assertEquals(3, CollectionUtil.getUsers().size()); // Including default manager
        assertEquals(1, CollectionUtil.getCustomers().size());
        assertEquals(1, CollectionUtil.getDeliveryPersons().size());
        assertEquals(1, CollectionUtil.getManagers().size());
    }
    
    @Test
    @DisplayName("Should retrieve user by ID")
    void testGetUserById() {
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        CollectionUtil.addUser(customer);
        
        User retrieved = CollectionUtil.getUserById("CUST001");
        
        assertNotNull(retrieved);
        assertEquals("John Doe", retrieved.getName());
    }
    
    @Test
    @DisplayName("Should return null for non-existent user")
    void testGetUserByIdNotFound() {
        User retrieved = CollectionUtil.getUserById("NONEXISTENT");
        assertNull(retrieved);
    }
    
    @Test
    @DisplayName("Should add food item to inventory")
    void testAddFoodItem() {
        FoodItem pizza = new FoodItem("Pizza", 12.99);
        
        CollectionUtil.addFoodItem(pizza, 10);
        
        Map<FoodItem, Integer> inventory = CollectionUtil.getInventory();
        assertTrue(inventory.containsKey(pizza));
        assertEquals(10, inventory.get(pizza));
    }
    
    @Test
    @DisplayName("Should accumulate quantities when adding same food item")
    void testAddFoodItemAccumulate() {
        FoodItem burger = new FoodItem("Burger", 8.99);
        
        CollectionUtil.addFoodItem(burger, 5);
        CollectionUtil.addFoodItem(burger, 3);
        
        Map<FoodItem, Integer> inventory = CollectionUtil.getInventory();
        assertEquals(8, inventory.get(burger));
    }
    
    @Test
    @DisplayName("Should update inventory when sufficient stock")
    void testUpdateInventorySuccess() {
        FoodItem pasta = new FoodItem("Pasta", 9.99);
        CollectionUtil.addFoodItem(pasta, 10);
        
        boolean updated = CollectionUtil.updateInventory(pasta, 3);
        
        assertTrue(updated);
        assertEquals(7, CollectionUtil.getInventory().get(pasta));
    }
    
    @Test
    @DisplayName("Should fail to update inventory when insufficient stock")
    void testUpdateInventoryInsufficient() {
        FoodItem salad = new FoodItem("Salad", 6.99);
        CollectionUtil.addFoodItem(salad, 2);
        
        boolean updated = CollectionUtil.updateInventory(salad, 5);
        
        assertFalse(updated);
        assertEquals(2, CollectionUtil.getInventory().get(salad));
    }
    
    @Test
    @DisplayName("Should find food item by name case-insensitive")
    void testFindFoodItemByName() {
        FoodItem coffee = new FoodItem("Coffee", 2.99);
        CollectionUtil.addFoodItem(coffee, 20);
        
        FoodItem found1 = CollectionUtil.findFoodItemByName("Coffee");
        FoodItem found2 = CollectionUtil.findFoodItemByName("coffee");
        FoodItem found3 = CollectionUtil.findFoodItemByName("COFFEE");
        
        assertNotNull(found1);
        assertNotNull(found2);
        assertNotNull(found3);
        assertEquals("Coffee", found1.getName());
    }
    
    @Test
    @DisplayName("Should return null for non-existent food item")
    void testFindFoodItemByNameNotFound() {
        FoodItem found = CollectionUtil.findFoodItemByName("NonExistent");
        assertNull(found);
    }
    
    @Test
    @DisplayName("Should add and retrieve orders")
    void testAddAndGetOrder() {
        Map<String, Integer> items = new HashMap<>();
        items.put("Pizza", 2);
        Order order = new Order("ORD001", "CUST001", "DEL001", items, 25.98);
        
        CollectionUtil.addOrder(order);
        
        Order retrieved = CollectionUtil.getOrderById("ORD001");
        assertNotNull(retrieved);
        assertEquals("ORD001", retrieved.getOrderId());
        assertEquals(25.98, retrieved.getTotalAmount());
    }
    
    @Test
    @DisplayName("Should clear all data except default manager")
    void testClearAllData() {
        // Add some test data
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        FoodItem pizza = new FoodItem("Pizza", 12.99);
        CollectionUtil.addUser(customer);
        CollectionUtil.addFoodItem(pizza, 10);
        
        CollectionUtil.clearAllData();
        
        // Should only have default manager
        assertEquals(1, CollectionUtil.getUsers().size());
        assertEquals(0, CollectionUtil.getCustomers().size());
        assertEquals(0, CollectionUtil.getDeliveryPersons().size());
        assertEquals(1, CollectionUtil.getManagers().size());
        assertEquals(0, CollectionUtil.getInventory().size());
        assertEquals(0, CollectionUtil.getOrders().size());
        
        // Default manager should still exist
        User defaultManager = CollectionUtil.getUserById("MGR001");
        assertNotNull(defaultManager);
    }
    
    @Test
    @DisplayName("Should initialize sample data successfully")
    void testInitializeSampleData() {
        CollectionUtil.initializeSampleData();
        
        Map<FoodItem, Integer> inventory = CollectionUtil.getInventory();
        assertFalse(inventory.isEmpty());
        
        // Check some sample items exist
        FoodItem burger = CollectionUtil.findFoodItemByName("Burger");
        FoodItem pizza = CollectionUtil.findFoodItemByName("Pizza");
        
        assertNotNull(burger);
        assertNotNull(pizza);
        assertEquals(5.99, burger.getPrice());
        assertEquals(12.99, pizza.getPrice());
    }
}