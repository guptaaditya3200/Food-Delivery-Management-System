package com.abes.foodDeliveryApplication.ui;

import java.util.List;
import java.util.Scanner;

import com.abes.foodDeliveryApplication.dto.Order;
import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.exception.InvalidOrderException;
import com.abes.foodDeliveryApplication.exception.UserNotFoundException;
import com.abes.foodDeliveryApplication.services.OrderService;
import com.abes.foodDeliveryApplication.services.UserService;
import com.abes.foodDeliveryApplication.utils.InputValidator;

public class DeliveryUI {
    private final Scanner scanner;
    private final UserService userService;
    private final OrderService orderService;
    
    public DeliveryUI(Scanner scanner, UserService userService, OrderService orderService) {
        this.scanner = scanner;
        this.userService = userService;
        this.orderService = orderService;
    }
    
    public void showDeliveryPersonMenu() {
        System.out.println("1. View My Orders");
        System.out.println("2. Complete Order");
        System.out.println("3. Toggle Availability");
    }
    
    public void showDeliveryStatus(User currentUser) {
        System.out.println("\n=== Your Delivery Status ===");
        List<Order> assignedOrders = orderService.getOrdersByDeliveryPersonId(currentUser.getId());
        
        if (assignedOrders.isEmpty()) {
            System.out.println("No deliveries assigned to you currently.");
        } else {
            System.out.println("Assigned Deliveries:");
            for (Order order : assignedOrders) {
                if (!"COMPLETED".equalsIgnoreCase(order.getStatus())) {
                    System.out.println("- Order ID: " + order.getOrderId() + 
                                     " | Customer ID: " + order.getCustomerId() + 
                                     " | Status: " + order.getStatus() + 
                                     " | Amount: $" + String.format("%.2f", order.getTotalAmount()));
                }
            }
        }
        System.out.println("Availability: " + (currentUser.isAvailable() ? "AVAILABLE" : "BUSY"));
        System.out.println("=============================");
    }
    
    public void handleDeliveryChoice(int choice, User currentUser) throws InvalidOrderException, UserNotFoundException {
        switch (choice) {
            case 1:
                viewMyDeliveries(currentUser);
                break;
            case 2:
                completeOrder();
                break;
            case 3:
                toggleAvailability(currentUser);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    public void registerDeliveryPerson() {
        System.out.println("\n=== Delivery Person Registration ===");
        
        String id = InputValidator.getValidInput(scanner, "Enter Delivery Person ID (min 3 chars): ", 
                                               "ID cannot be empty and must be at least 3 characters long.");
        
        if (!InputValidator.isValidId(id)) {
            System.out.println("Invalid ID format.");
            return;
        }
        
        // Check if user ID already exists
        try {
            userService.getUserById(id);
            System.out.println("Error: User ID already exists. Please choose a different ID.");
            return;
        } catch (UserNotFoundException e) {
            // ID doesn't exist, which is good - we can proceed
        }
        
        String name = InputValidator.getValidInput(scanner, "Enter Name: ", "Name cannot be empty.");
        
        String email;
        do {
            email = InputValidator.getValidInput(scanner, "Enter Email: ", "Email cannot be empty.");
            if (!InputValidator.isValidEmail(email)) {
                System.out.println("Invalid email format. Please try again.");
            }
        } while (!InputValidator.isValidEmail(email));
        
        String phone;
        do {
            phone = InputValidator.getValidInput(scanner, "Enter Phone (10 digits): ", "Phone cannot be empty.");
            if (!InputValidator.isValidPhone(phone)) {
                System.out.println("Invalid phone format. Please enter 10 digits.");
            }
        } while (!InputValidator.isValidPhone(phone));
        
        String password;
        do {
            password = InputValidator.getValidInput(scanner, "Enter Password (min 6 chars): ", "Password cannot be empty.");
            if (!InputValidator.isValidPassword(password)) {
                System.out.println("Password must be at least 6 characters long.");
            }
        } while (!InputValidator.isValidPassword(password));
        
        userService.registerDeliveryPerson(id, name, email, phone, password);
        System.out.println("Delivery Person registered successfully!");
    }
    
    private void viewMyDeliveries(User currentUser) {
        System.out.println("\n=== My Assigned Deliveries ===");
        List<Order> assignedOrders = orderService.getOrdersByDeliveryPersonId(currentUser.getId());
        
        if (assignedOrders.isEmpty()) {
            System.out.println("No deliveries assigned to you.");
            return;
        }
        
        for (Order order : assignedOrders) {
            System.out.println(orderService.getOrderDetails(order.getOrderId()));
            System.out.println("------------------------");
        }
    }
    
    private void completeOrder() throws InvalidOrderException {
        System.out.print("Enter Order ID to complete: ");
        String orderId = scanner.nextLine().trim();
        
        orderService.completeOrder(orderId);
        System.out.println("Order completed successfully!");
    }
    
    private void toggleAvailability(User currentUser) throws UserNotFoundException {
        boolean newStatus = !currentUser.isAvailable();
        userService.updateUserAvailability(currentUser.getId(), newStatus);
        currentUser.setAvailable(newStatus);
        
        System.out.println("Availability updated. You are now " + 
                          (newStatus ? "AVAILABLE" : "UNAVAILABLE") + " for deliveries.");
    }
}