package com.abes.foodDeliveryApplication.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    
    @Test
    @DisplayName("Should validate correct email addresses")
    void testValidEmails() {
        assertTrue(InputValidator.isValidEmail("test@example.com"));
        assertTrue(InputValidator.isValidEmail("user.name@domain.co.uk"));
        assertTrue(InputValidator.isValidEmail("user+tag@example.org"));
        assertTrue(InputValidator.isValidEmail("123@example.com"));
    }
    
    @Test
    @DisplayName("Should reject invalid email addresses")
    void testInvalidEmails() {
        assertFalse(InputValidator.isValidEmail("invalid-email"));
        assertFalse(InputValidator.isValidEmail("@example.com"));
        assertFalse(InputValidator.isValidEmail("test@"));
        assertFalse(InputValidator.isValidEmail("test@.com"));
        assertFalse(InputValidator.isValidEmail(""));
    }
    
    @Test
    @DisplayName("Should validate correct phone numbers")
    void testValidPhones() {
        assertTrue(InputValidator.isValidPhone("1234567890"));
        assertTrue(InputValidator.isValidPhone("9876543210"));
        assertTrue(InputValidator.isValidPhone("0000000000"));
    }
    
    @Test
    @DisplayName("Should reject invalid phone numbers")
    void testInvalidPhones() {
        assertFalse(InputValidator.isValidPhone("123456789")); // 9 digits
        assertFalse(InputValidator.isValidPhone("12345678901")); // 11 digits
        assertFalse(InputValidator.isValidPhone("abcdefghij"));
        assertFalse(InputValidator.isValidPhone("123-456-7890"));
        assertFalse(InputValidator.isValidPhone(""));
    }
    
    @Test
    @DisplayName("Should validate correct IDs")
    void testValidIds() {
        assertTrue(InputValidator.isValidId("CUST001"));
        assertTrue(InputValidator.isValidId("DEL123"));
        assertTrue(InputValidator.isValidId("abc"));
        assertTrue(InputValidator.isValidId("a1b2c3"));
    }
    
    @Test
    @DisplayName("Should reject invalid IDs")
    void testInvalidIds() {
        assertFalse(InputValidator.isValidId("ab")); // Too short
        assertFalse(InputValidator.isValidId("  ")); // Only spaces
        assertFalse(InputValidator.isValidId(""));
        assertFalse(InputValidator.isValidId(null));
    }
    
    @Test
    @DisplayName("Should validate correct passwords")
    void testValidPasswords() {
        assertTrue(InputValidator.isValidPassword("password123"));
        assertTrue(InputValidator.isValidPassword("123456"));
        assertTrue(InputValidator.isValidPassword("abcdef"));
        assertTrue(InputValidator.isValidPassword("P@ssw0rd!"));
    }
    
    @Test
    @DisplayName("Should reject invalid passwords")
    void testInvalidPasswords() {
        assertFalse(InputValidator.isValidPassword("12345")); // Too short
        assertFalse(InputValidator.isValidPassword(""));
        assertFalse(InputValidator.isValidPassword(null));
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {0.01, 1.0, 99.99, 1000.0})
    @DisplayName("Should validate positive prices")
    void testValidPrices(double price) {
        assertTrue(InputValidator.isValidPrice(price));
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -99.99})
    @DisplayName("Should reject non-positive prices")
    void testInvalidPrices(double price) {
        assertFalse(InputValidator.isValidPrice(price));
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 100, 1000})
    @DisplayName("Should validate positive quantities")
    void testValidQuantities(int quantity) {
        assertTrue(InputValidator.isValidQuantity(quantity));
    }
    
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    @DisplayName("Should reject non-positive quantities")
    void testInvalidQuantities(int quantity) {
        assertFalse(InputValidator.isValidQuantity(quantity));
    }
}