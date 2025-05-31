package com.abes.foodDeliveryApplication.utils;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.services.FoodService;
import com.abes.foodDeliveryApplication.services.FoodServiceImpl;
import com.abes.foodDeliveryApplication.dto.FoodItem;
import com.abes.foodDeliveryApplication.dto.Order;
import java.util.*;

public class CollectionUtil {
    // Storage for all entities
    private static final Map<String, User> users = new HashMap<>();
    private static final Map<String, User> customers = new HashMap<>();
    private static final Map<String, User> deliveryPersons = new HashMap<>();
    private static final Map<String, User> managers = new HashMap<>();
    private static final Map<FoodItem, Integer> inventory = new HashMap<>();
    private static final Map<String, Order> orders = new HashMap<>();
    
    // Static initialization with default manager
    static {
        User defaultManager = new User("MGR001", "System Manager", "manager@system.com", 
                                     "9999999999", "admin123", "MANAGER");
        managers.put(defaultManager.getId(), defaultManager);
        users.put(defaultManager.getId(), defaultManager);
    }
    
    // User operations
    public static Map<String, User> getUsers() {
        return users;
    }
    
    public static Map<String, User> getCustomers() {
        return customers;
    }
    
    public static Map<String, User> getDeliveryPersons() {
        return deliveryPersons;
    }
    
    public static Map<String, User> getManagers() {
        return managers;
    }
    
    public static void addUser(User user) {
        users.put(user.getId(), user);
        switch (user.getRole().toLowerCase()) {
            case "customer":
                customers.put(user.getId(), user);
                break;
            case "delivery":
                deliveryPersons.put(user.getId(), user);
                break;
            case "manager":
                managers.put(user.getId(), user);
                break;
        }
    }
    
    public static User getUserById(String id) {
        return users.get(id);
    }
    
    // Inventory operations
    public static Map<FoodItem, Integer> getInventory() {
        return inventory;
    }
    
    public static void addFoodItem(FoodItem item, int quantity) {
        inventory.put(item, inventory.getOrDefault(item, 0) + quantity);
    }
    
    public static boolean updateInventory(FoodItem item, int quantity) {
        if (inventory.containsKey(item) && inventory.get(item) >= quantity) {
            inventory.put(item, inventory.get(item) - quantity);
            return true;
        }
        return false;
    }
    
    public static FoodItem findFoodItemByName(String name) {
        return inventory.keySet().stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    // Order operations
    public static Map<String, Order> getOrders() {
        return orders;
    }
    
    public static void addOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }
    
    public static Order getOrderById(String orderId) {
        return orders.get(orderId);
    }
    
    // Utility methods
    public static void clearAllData() {
        users.clear();
        customers.clear();
        deliveryPersons.clear();
        inventory.clear();
        orders.clear();
        // Re-add default manager
        User defaultManager = new User("MGR001", "System Manager", "manager@system.com", 
                                     "9999999999", "admin123", "MANAGER");
        managers.put(defaultManager.getId(), defaultManager);
        users.put(defaultManager.getId(), defaultManager);
    }
    
    public static void initializeSampleData() {
        FoodService foodService = new FoodServiceImpl();
        
        // Add sample food items
        foodService.addNewFoodItem("Burger", 5.99, 50);
        foodService.addNewFoodItem("Pizza", 12.99, 30);
        foodService.addNewFoodItem("Pasta", 8.99, 40);
        foodService.addNewFoodItem("Salad", 6.99, 25);
        foodService.addNewFoodItem("Sandwich", 4.99, 35);
        foodService.addNewFoodItem("French Fries", 3.99, 60);
        foodService.addNewFoodItem("Soda", 1.99, 100);
        foodService.addNewFoodItem("Coffee", 2.99, 80);
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Default Manager - ID: MGR001, Password: admin123");
    }
}