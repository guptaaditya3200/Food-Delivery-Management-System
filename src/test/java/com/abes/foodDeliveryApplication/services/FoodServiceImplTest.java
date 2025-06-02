package com.abes.foodDeliveryApplication.services;

import com.abes.foodDeliveryApplication.dto.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

@DisplayName("FoodService Tests")
class FoodServiceImplTest {
    
    private FoodService foodService;
    
    @BeforeEach
    void setUp() {
        foodService = new FoodServiceImpl();
    }
    
    @Nested
    @DisplayName("Add New Food Item Tests")
    class AddNewFoodItemTests {
        
        @Test
        @DisplayName("Should add new food item successfully")
        void shouldAddNewFoodItemSuccessfully() {
            // Given
            String name = "Pizza";
            double price = 12.99;
            int quantity = 10;
            
            // When
            assertDoesNotThrow(() -> foodService.addNewFoodItem(name, price, quantity));
            
            // Then
            FoodItem retrievedItem = foodService.getFoodItemByName(name);
            assertNotNull(retrievedItem);
            assertEquals(name, retrievedItem.getName());
            assertEquals(price, retrievedItem.getPrice());
        }
        
        @Test
        @DisplayName("Should add multiple different food items")
        void shouldAddMultipleDifferentFoodItems() {
            // Given & When
            foodService.addNewFoodItem("Burger", 8.99, 5);
            foodService.addNewFoodItem("Fries", 3.99, 15);
            foodService.addNewFoodItem("Coke", 1.99, 20);
            
            // Then
            Map<FoodItem, Integer> menu = foodService.getMenu();
            assertEquals(3, menu.size());
        }
    }
    
    @Nested
    @DisplayName("Restock Item Tests")
    class RestockItemTests {
        
        @Test
        @DisplayName("Should restock existing item successfully")
        void shouldRestockExistingItemSuccessfully() {
            // Given
            String itemName = "Sandwich";
            foodService.addNewFoodItem(itemName, 6.99, 5);
            
            // When
            assertDoesNotThrow(() -> foodService.restockItem(itemName, 10));
            
            // Then
            assertTrue(foodService.isItemAvailable(itemName, 15)); // 5 + 10 = 15
        }
        
        @Test
        @DisplayName("Should handle restocking non-existent item")
        void shouldHandleRestockingNonExistentItem() {
            // Given
            String nonExistentItem = "NonExistentItem";
            
            // When & Then
            assertDoesNotThrow(() -> foodService.restockItem(nonExistentItem, 5));
        }
    }
    
    @Nested
    @DisplayName("Get Food Item By Name Tests")
    class GetFoodItemByNameTests {
        
        @Test
        @DisplayName("Should return food item when exists")
        void shouldReturnFoodItemWhenExists() {
            // Given
            String itemName = "Steak";
            double price = 24.99;
            foodService.addNewFoodItem(itemName, price, 3);
            
            // When
            FoodItem item = foodService.getFoodItemByName(itemName);
            
            // Then
            assertNotNull(item);
            assertEquals(itemName, item.getName());
            assertEquals(price, item.getPrice());
        }
        
        @Test
        @DisplayName("Should return null when item does not exist")
        void shouldReturnNullWhenItemDoesNotExist() {
            // When
            FoodItem item = foodService.getFoodItemByName("NonExistentItem");
            
            // Then
            assertNull(item);
        }
    }
    
    @Nested
    @DisplayName("Item Availability Tests")
    class ItemAvailabilityTests {
        
        @Test
        @DisplayName("Should return true when item is available in sufficient quantity")
        void shouldReturnTrueWhenItemIsAvailableInSufficientQuantity() {
            // Given
            String itemName = "Chicken";
            foodService.addNewFoodItem(itemName, 15.99, 10);
            
            // When & Then
            assertTrue(foodService.isItemAvailable(itemName, 5));
            assertTrue(foodService.isItemAvailable(itemName, 10));
        }
        
        @Test
        @DisplayName("Should return false when item is not available in sufficient quantity")
        void shouldReturnFalseWhenItemIsNotAvailableInSufficientQuantity() {
            // Given
            String itemName = "Fish";
            foodService.addNewFoodItem(itemName, 18.99, 3);
            
            // When & Then
            assertFalse(foodService.isItemAvailable(itemName, 5));
        }
        
        @Test
        @DisplayName("Should return false when item does not exist")
        void shouldReturnFalseWhenItemDoesNotExist() {
            // When & Then
            assertFalse(foodService.isItemAvailable("NonExistentItem", 1));
        }
    }
}