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
        User defaultManager = new User("MGR001", "System Manager", "manager@system.com",
                "9999999999", "admin123", "MANAGER");
        managers.put(defaultManager.getId(), defaultManager);
        users.put(defaultManager.getId(), defaultManager);
    }

}