package com.abes.foodDeliveryApplication.utils;

import com.abes.foodDeliveryApplication.dto.User;
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
        User defaultManager = new User("M001", "Rayush", "rayush@gmail.com",
                "8840414007", "123456789", "MANAGER");
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

}