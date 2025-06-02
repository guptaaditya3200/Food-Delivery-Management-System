package com.abes.foodDeliveryApplication.dao;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.exception.UserNotFoundException;
import com.abes.foodDeliveryApplication.exception.InvalidCredentialsException;
import com.abes.foodDeliveryApplication.utils.CollectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class UserDaoImplTest {
    
    private UserDao userDao;
    
    @BeforeEach
    void setUp() {
        CollectionUtil.clearAllData();
        userDao = new UserDaoImpl();
    }
    
    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUser() {
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        
        assertDoesNotThrow(() -> userDao.registerUser(customer));
        
        assertDoesNotThrow(() -> {
            User retrieved = userDao.getUserById("CUST001");
            assertEquals("John Doe", retrieved.getName());
        });
    }
    
    @Test
    @DisplayName("Should authenticate user with correct credentials")
    void testAuthenticateUserSuccess() {
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        userDao.registerUser(customer);
        
        assertDoesNotThrow(() -> {
            User authenticated = userDao.authenticateUser("CUST001", "password123");
            assertEquals("John Doe", authenticated.getName());
        });
    }
    
    @Test
    @DisplayName("Should throw UserNotFoundException for non-existent user")
    void testAuthenticateUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userDao.authenticateUser("NONEXISTENT", "password");
        });
    }
    
    @Test
    @DisplayName("Should throw InvalidCredentialsException for wrong password")
    void testAuthenticateUserWrongPassword() {
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        userDao.registerUser(customer);
        
        assertThrows(InvalidCredentialsException.class, () -> {
            userDao.authenticateUser("CUST001", "wrongpassword");
        });
    }
    
    @Test
    @DisplayName("Should get user by ID")
    void testGetUserById() {
        User delivery = new User("DEL001", "Jane Smith", "jane@example.com", "9876543210", "password456", "DELIVERY");
        userDao.registerUser(delivery);
        
        assertDoesNotThrow(() -> {
            User retrieved = userDao.getUserById("DEL001");
            assertEquals("Jane Smith", retrieved.getName());
            assertEquals("DELIVERY", retrieved.getRole());
        });
    }
    
    @Test
    @DisplayName("Should throw UserNotFoundException for non-existent user ID")
    void testGetUserByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userDao.getUserById("NONEXISTENT");
        });
    }
    
    @Test
    @DisplayName("Should get all users by role")
    void testGetAllUsersByRole() {
        User customer1 = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        User customer2 = new User("CUST002", "Alice Smith", "alice@example.com", "1111111111", "password456", "CUSTOMER");
        User delivery = new User("DEL001", "Bob Wilson", "bob@example.com", "2222222222", "password789", "DELIVERY");
        
        userDao.registerUser(customer1);
        userDao.registerUser(customer2);
        userDao.registerUser(delivery);
        
        List<User> customers = userDao.getAllUsersByRole("CUSTOMER");
        List<User> deliveryPersons = userDao.getAllUsersByRole("DELIVERY");
        
        assertEquals(2, customers.size());
        assertEquals(1, deliveryPersons.size());
        assertTrue(customers.stream().allMatch(u -> u.getRole().equalsIgnoreCase("CUSTOMER")));
    }
    
    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser() {
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        userDao.registerUser(customer);
        
        customer.setName("John Updated");
        customer.setEmail("johnupdated@example.com");
        
        assertDoesNotThrow(() -> {
            userDao.updateUser(customer);
            User updated = userDao.getUserById("CUST001");
            assertEquals("John Updated", updated.getName());
            assertEquals("johnupdated@example.com", updated.getEmail());
        });
    }
    
    @Test
    @DisplayName("Should throw UserNotFoundException when updating non-existent user")
    void testUpdateUserNotFound() {
        User nonExistent = new User("NONEXISTENT", "Test", "test@example.com", "1111111111", "password", "CUSTOMER");
        
        assertThrows(UserNotFoundException.class, () -> {
            userDao.updateUser(nonExistent);
        });
    }
    
    @Test
    @DisplayName("Should remove user successfully")
    void testRemoveUser() {
        User customer = new User("CUST001", "John Doe", "john@example.com", "1234567890", "password123", "CUSTOMER");
        userDao.registerUser(customer);
        
        assertDoesNotThrow(() -> userDao.removeUser("CUST001"));
        
        assertThrows(UserNotFoundException.class, () -> {
            userDao.getUserById("CUST001");
        });
    }
    
    @Test
    @DisplayName("Should throw UserNotFoundException when removing non-existent user")
    void testRemoveUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userDao.removeUser("NONEXISTENT");
        });
    }
}