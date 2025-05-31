package com.abes.foodDeliveryApplication.services;

import com.abes.foodDeliveryApplication.dto.FoodItem;
import java.util.Map;

public interface FoodService {
    void addNewFoodItem(String name, double price, int quantity);
    void restockItem(String itemName, int quantity);
    Map<FoodItem, Integer> getMenu();
    FoodItem getFoodItemByName(String name);
    boolean isItemAvailable(String itemName, int requestedQuantity);
}

