package com.abes.foodDeliveryApplication.dao;

import com.abes.foodDeliveryApplication.dto.FoodItem;
import java.util.Map;

public interface FoodDao {
    void addFoodItem(FoodItem item, int quantity);
    Map<FoodItem, Integer> getAllFoodItems();
    FoodItem getFoodItemByName(String name);
    boolean updateInventory(FoodItem item, int quantity);
    void restockItem(String itemName, int quantity);
}