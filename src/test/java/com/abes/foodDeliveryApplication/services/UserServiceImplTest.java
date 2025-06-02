package com.abes.foodDeliveryApplication.services;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.exception.UserNotFoundException;
import com.abes.foodDeliveryApplication.exception.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DisplayName("UserService Tests")
class UserServiceImplTest {
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }
    
    @Nested
    @DisplayName("Register Customer Tests")
    class RegisterCustomerTests {
        
        @Test
        @DisplayName("Should register customer successfully")
        void shouldRegisterCustomerSuccessfully() {
            // Given
            String id = "CUST001";
            String name = "Alice Johnson";
            String email = "alice@example.com";
            String phone = "9876543210";
            String password = "password123";
            
            // When & Then
            assertDoesNotThrow(() -> userService.registerCustomer(id, name, email, phone, password));
        }
        
        @Test
        @DisplayName("Should retrieve registered customer")
        void shouldRetrieveRegisteredCustomer() {
            // Given
            String id = "CUST002";
            String name = "Bob Smith";
            String email = "bob@example.com";
            String phone = "9876543211";
            String password = "password456";
            
            userService.registerCustomer(id, name, email, phone, password);
            
            // When
            assertDoesNotThrow(() -> {
                User customer = userService.getUserById(id);
                
                // Then
                assertNotNull(customer);
                assertEquals(id, customer.getId());
                assertEquals(name, customer.getName());
                assertEquals(email, customer.getEmail());
                assertEquals("CUSTOMER", customer.getRole());
            });
        }
    }
    
    @Nested
    @DisplayName("Register Delivery Person Tests")
    class RegisterDeliveryPersonTests {
        
        @Test
        @DisplayName("Should register delivery person successfully")
        void shouldRegisterDeliveryPersonSuccessfully() {
            // Given
            String id = "DEL001";
            String name = "Charlie Brown";
            String email = "charlie@example.com";
            String phone = "9876543212";
            String password = "password789";
            
            // When & Then
            assertDoesNotThrow(() -> userService.registerDeliveryPerson(id, name, email, phone, password));
        }
        
        @Test
        @DisplayName("Should retrieve registered delivery person")
        void shouldRetrieveRegisteredDeliveryPerson() {
            // Given
            String id = "DEL002";
            String name = "Diana Prince";
            String email = "diana@example.com";
            String phone = "9876543213";
            String password = "password101";
            
            userService.registerDeliveryPerson(id, name, email, phone, password);
            
            // When
            assertDoesNotThrow(() -> {
                User deliveryPerson = userService.getUserById(id);
                
                // Then
                assertNotNull(deliveryPerson);
                assertEquals(id, deliveryPerson.getId());
                assertEquals(name, deliveryPerson.getName());
                assertEquals("DELIVERY", deliveryPerson.getRole());
            });
        }
    }
    
    @Nested
    @DisplayName("Authentication Tests")
    class AuthenticationTests {
        
        @Test
        @DisplayName("Should authenticate user with correct credentials")
        void shouldAuthenticateUserWithCorrectCredentials() {
            // Given
            String id = "USER001";
            String password = "correctPassword";
            userService.registerCustomer(id, "Test User", "test@example.com", "1234567890", password);
            
            // When & Then
            assertDoesNotThrow(() -> {
                User authenticatedUser = userService.authenticateUser(id, password);
                assertNotNull(authenticatedUser);
                assertEquals(id, authenticatedUser.getId());
            });
        }
        
        @Test
        @DisplayName("Should throw exception for non-existent user")
        void shouldThrowExceptionForNonExistentUser() {
            // When & Then
            assertThrows(UserNotFoundException.class, 
                () -> userService.authenticateUser("NONEXISTENT", "password"));
        }
    }
    
    @Nested
    @DisplayName("Get User By ID Tests")
    class GetUserByIdTests {
        
        @Test
        @DisplayName("Should return user when exists")
        void shouldReturnUserWhenExists() {
            // Given
            String userId = "USER123";
            userService.registerCustomer(userId, "John Doe", "john@example.com", "1234567890", "password");
            
            // When & Then
            assertDoesNotThrow(() -> {
                User user = userService.getUserById(userId);
                assertNotNull(user);
                assertEquals(userId, user.getId());
            });
        }
        
        @Test
        @DisplayName("Should throw exception when user does not exist")
        void shouldThrowExceptionWhenUserDoesNotExist() {
            // When & Then
            assertThrows(UserNotFoundException.class, 
                () -> userService.getUserById("INVALID_USER"));
        }
    }
    
    @Nested
    @DisplayName("Delivery Person Management Tests")
    class DeliveryPersonManagementTests {
        
        @Test
        @DisplayName("Should get all delivery persons")
        void shouldGetAllDeliveryPersons() {
            // Given
            userService.registerDeliveryPerson("DEL001", "Delivery1", "del1@example.com", "1111111111", "pass1");
            userService.registerDeliveryPerson("DEL002", "Delivery2", "del2@example.com", "2222222222", "pass2");
            userService.registerCustomer("CUST001", "Customer1", "cust1@example.com", "3333333333", "pass3");
            
            // When
            List<User> deliveryPersons = userService.getAllDeliveryPersons();
            
            // Then
            assertNotNull(deliveryPersons);
            assertEquals(2, deliveryPersons.size());
            assertTrue(deliveryPersons.stream().allMatch(user -> "DELIVERY".equals(user.getRole())));
        }
        
        @Test
        @DisplayName("Should get available delivery persons")
        void shouldGetAvailableDeliveryPersons() {
            // Given
            userService.registerDeliveryPerson("DEL001", "Available", "avail@example.com", "1111111111", "pass1");
            userService.registerDeliveryPerson("DEL002", "Unavailable", "unavail@example.com", "2222222222", "pass2");
            
            // Make one unavailable
            assertDoesNotThrow(() -> userService.updateUserAvailability("DEL002", false));
            
            // When
            List<User> availableDeliveryPersons = userService.getAvailableDeliveryPersons();
            
            // Then
            assertNotNull(availableDeliveryPersons);
            assertEquals(1, availableDeliveryPersons.size());
            assertTrue(availableDeliveryPersons.get(0).isAvailable());
        }
        
        @Test
        @DisplayName("Should update user availability")
        void shouldUpdateUserAvailability() {
            // Given
            String userId = "DEL001";
            userService.registerDeliveryPerson(userId, "Test Delivery", "test@example.com", "1234567890", "password");
            
            // When & Then
            assertDoesNotThrow(() -> {
                userService.updateUserAvailability(userId, false);
                User user = userService.getUserById(userId);
                assertFalse(user.isAvailable());
                
                userService.updateUserAvailability(userId, true);
                user = userService.getUserById(userId);
                assertTrue(user.isAvailable());
            });
        }
        
        @Test
        @DisplayName("Should remove delivery person")
        void shouldRemoveDeliveryPerson() {
            // Given
            String deliveryPersonId = "DEL001";
            userService.registerDeliveryPerson(deliveryPersonId, "To Be Removed", "remove@example.com", "1234567890", "password");
            
            // When & Then
            assertDoesNotThrow(() -> userService.removeDeliveryPerson(deliveryPersonId));
            
            // Verify removal
            assertThrows(UserNotFoundException.class, 
                () -> userService.getUserById(deliveryPersonId));
        }
        
        @Test
        @DisplayName("Should throw exception when trying to remove non-delivery person")
        void shouldThrowExceptionWhenTryingToRemoveNonDeliveryPerson() {
            // Given
            String customerId = "CUST001";
            userService.registerCustomer(customerId, "Customer", "customer@example.com", "1234567890", "password");
            
            // When & Then
            assertThrows(UserNotFoundException.class, 
                () -> userService.removeDeliveryPerson(customerId));
        }
    }
}