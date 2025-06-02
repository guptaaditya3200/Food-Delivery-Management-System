package com.abes.foodDeliveryApplication.dao;
import com.abes.foodDeliveryApplication.dto.FoodItem;
import com.abes.foodDeliveryApplication.utils.CollectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

class FoodDaoImplTest {
    
    private FoodDao foodDao;
    
    @BeforeEach
    void setUp() {
        CollectionUtil.clearAllData();
        foodDao = new FoodDaoImpl();
    }
    
    @Test
    @DisplayName("Should add food item successfully")
    void testAddFoodItem() {
        FoodItem pizza = new FoodItem("Pizza", 12.99);
        
        foodDao.addFoodItem(pizza, 10);
        
        Map<FoodItem, Integer> inventory = foodDao.getAllFoodItems();
        assertTrue(inventory.containsKey(pizza));
        assertEquals(10, inventory.get(pizza));
    }
    
    @Test
    @DisplayName("Should get all food items")
    void testGetAllFoodItems() {
        FoodItem burger = new FoodItem("Burger", 8.99);
        FoodItem pizza = new FoodItem("Pizza", 12.99);
        
        foodDao.addFoodItem(burger, 5);
        foodDao.addFoodItem(pizza, 3);
        
        Map<FoodItem, Integer> inventory = foodDao.getAllFoodItems();
        assertEquals(2, inventory.size());
        assertTrue(inventory.containsKey(burger));
        assertTrue(inventory.containsKey(pizza));
    }
    
    @Test
    @DisplayName("Should find food item by name")
    void testGetFoodItemByName() {
        FoodItem pasta = new FoodItem("Pasta", 9.99);
        foodDao.addFoodItem(pasta, 15);
        
        FoodItem found = foodDao.getFoodItemByName("Pasta");
        assertNotNull(found);
        assertEquals("Pasta", found.getName());
        assertEquals(9.99, found.getPrice());
    }
    
    @Test
    @DisplayName("Should return null for non-existent food item")
    void testGetFoodItemByNameNotFound() {
        FoodItem found = foodDao.getFoodItemByName("NonExistent");
        assertNull(found);
    }
    
    @Test
    @DisplayName("Should update inventory successfully")
    void testUpdateInventory() {
        FoodItem burger = new FoodItem("Burger", 8.99);
        foodDao.addFoodItem(burger, 10);
        
        boolean updated = foodDao.updateInventory(burger, 3);
        
        assertTrue(updated);
        Map<FoodItem, Integer> inventory = foodDao.getAllFoodItems();
        assertEquals(7, inventory.get(burger));
    }
    
    @Test
    @DisplayName("Should fail to update inventory with insufficient stock")
    void testUpdateInventoryInsufficientStock() {
        FoodItem burger = new FoodItem("Burger", 8.99);
        foodDao.addFoodItem(burger, 5);
        
        boolean updated = foodDao.updateInventory(burger, 10);
        
        assertFalse(updated);
        Map<FoodItem, Integer> inventory = foodDao.getAllFoodItems();
        assertEquals(5, inventory.get(burger));
    }
    
    @Test
    @DisplayName("Should restock existing item")
    void testRestockItem() {
        FoodItem pizza = new FoodItem("Pizza", 12.99);
        foodDao.addFoodItem(pizza, 5);
        
        foodDao.restockItem("Pizza", 10);
        
        Map<FoodItem, Integer> inventory = foodDao.getAllFoodItems();
        assertEquals(15, inventory.get(pizza));
    }
    
    @Test
    @DisplayName("Should handle restocking non-existent item")
    void testRestockNonExistentItem() {
        assertDoesNotThrow(() -> foodDao.restockItem("NonExistent", 10));
    }
}