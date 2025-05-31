package com.abes.foodDeliveryApplication.ui;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.dto.FoodItem;
import com.abes.foodDeliveryApplication.dto.Order;
import com.abes.foodDeliveryApplication.services.*;
import com.abes.foodDeliveryApplication.exception.*;
import com.abes.foodDeliveryApplication.utils.InputValidator;

import java.util.*;

public class MenuHandler {
    private final Scanner scanner;
    private final UserService userService;
    private User currentUser;
    
    // UI components
    private final UserUI userUI;
    private final ManagerUI managerUI;
    private final DeliveryUI deliveryUI;
    
    public MenuHandler() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserServiceImpl();
        FoodService foodService = new FoodServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        ManagerService managerService = new ManagerServiceImpl();
        
        // Initialize UI components
        this.userUI = new UserUI(scanner, userService, foodService, orderService);
        this.managerUI = new ManagerUI(scanner, userService, foodService, managerService);
        this.deliveryUI = new DeliveryUI(scanner, userService, orderService);
    }
    
    public void start() {
        System.out.println("=== Welcome to Online Food Delivery System ===");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private void showLoginMenu() {
        System.out.println("\n=== Login/Registration Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register Customer");
        System.out.println("3. Register Delivery Person");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                userUI.registerCustomer();
                break;
            case 3:
                deliveryUI.registerDeliveryPerson();
                break;
            case 4:
                System.out.println("Thank you for using Food Delivery System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        
        // Show delivery status for delivery personnel at the top
        if ("DELIVERY".equalsIgnoreCase(currentUser.getRole())) {
            deliveryUI.showDeliveryStatus(currentUser);
        }
        
        if ("CUSTOMER".equalsIgnoreCase(currentUser.getRole())) {
            userUI.showCustomerMenu();
        } else if ("MANAGER".equalsIgnoreCase(currentUser.getRole())) {
            managerUI.showManagerMenu();
        } else if ("DELIVERY".equalsIgnoreCase(currentUser.getRole())) {
            deliveryUI.showDeliveryPersonMenu();
        }
        
        System.out.println("0. Logout");
        System.out.print("Choose an option: ");
        
        int choice = getIntInput();
        handleMenuChoice(choice);
    }
    
    private void handleMenuChoice(int choice) {
        if (choice == 0) {
            logout();
            return;
        }
        
        try {
            if ("CUSTOMER".equalsIgnoreCase(currentUser.getRole())) {
                userUI.handleCustomerChoice(choice, currentUser);
            } else if ("MANAGER".equalsIgnoreCase(currentUser.getRole())) {
                managerUI.handleManagerChoice(choice);
            } else if ("DELIVERY".equalsIgnoreCase(currentUser.getRole())) {
                deliveryUI.handleDeliveryChoice(choice, currentUser);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void login() {
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        try {
            currentUser = userService.authenticateUser(id, password);
            System.out.println("Login successful! Welcome " + currentUser.getName());
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
    
    private void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }
    
    private int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}