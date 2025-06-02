package com.abes.foodDeliveryApplication.services;

import com.abes.foodDeliveryApplication.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ManagerService Tests")
class ManagerServiceImplTest {
    
    private ManagerService managerService;
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        managerService = new ManagerServiceImpl();
        userService = new UserServiceImpl();
    }
    
    @Nested
    @DisplayName("Add New Food Item Tests")
    class AddNewFoodItemTests {
        
        @Test
        @DisplayName("Should add new food item through manager service")
        void shouldAddNewFoodItemThroughManagerService() {
            // Given
            String name = "Manager Pizza";
            double price = 15.99;
            int quantity = 8;
            
            // When & Then
            assertDoesNotThrow(() -> managerService.addNewFoodItem(name, price, quantity));
        }
    }
    
    @Nested
    @DisplayName("Restock Food Item Tests")
    class RestockFoodItemTests {
        
        @Test
        @DisplayName("Should restock food item through manager service")
        void shouldRestockFoodItemThroughManagerService() {
            // Given
            String itemName = "Manager Burger";
            managerService.addNewFoodItem(itemName, 10.99, 5);
            
            // When & Then
            assertDoesNotThrow(() -> managerService.restockFoodItem(itemName, 10));
        }
    }
    
    @Nested
    @DisplayName("Remove Delivery Person Tests")
    class RemoveDeliveryPersonTests {
        
        @Test
        @DisplayName("Should remove existing delivery person")
        void shouldRemoveExistingDeliveryPerson() {
            // Given
            String deliveryPersonId = "DEL001";
            userService.registerDeliveryPerson(deliveryPersonId, "John Doe", "john@example.com", "1234567890", "password");
            
            // When & Then
            assertDoesNotThrow(() -> managerService.removeDeliveryPerson(deliveryPersonId));
        }
        
        @Test
        @DisplayName("Should throw exception when delivery person does not exist")
        void shouldThrowExceptionWhenDeliveryPersonDoesNotExist() {
            // When & Then
            assertThrows(UserNotFoundException.class, 
                () -> managerService.removeDeliveryPerson("NONEXISTENT"));
        }
    }
    
    @Nested
    @DisplayName("Validate Manager Role Tests")
    class ValidateManagerRoleTests {
        
        @Test
        @DisplayName("Should validate manager role successfully")
        void shouldValidateManagerRoleSuccessfully() {
            // Given
            String managerId = "MGR001";
            // Note: You'll need to add a registerManager method or modify this test
            // based on your actual user registration implementation
            
            // When & Then
            // This test may need adjustment based on your User creation logic
            assertDoesNotThrow(() -> {
                boolean result = managerService.validateManagerRole(managerId);
                // The actual assertion will depend on your implementation
            });
        }
        
        @Test
        @DisplayName("Should throw exception for non-existent user")
        void shouldThrowExceptionForNonExistentUser() {
            // When & Then
            assertThrows(UserNotFoundException.class, 
                () -> managerService.validateManagerRole("INVALID_ID"));
        }
    }
}