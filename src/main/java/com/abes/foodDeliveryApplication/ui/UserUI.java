package com.abes.foodDeliveryApplication.ui;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.dto.FoodItem;
import com.abes.foodDeliveryApplication.dto.Order;
import com.abes.foodDeliveryApplication.services.*;
import com.abes.foodDeliveryApplication.exception.*;
import com.abes.foodDeliveryApplication.utils.InputValidator;

import java.util.*;

public class UserUI {
    private final Scanner scanner;
    private final UserService userService;
    private final FoodService foodService;
    private final OrderService orderService;
    
    public UserUI(Scanner scanner, UserService userService, FoodService foodService, OrderService orderService) {
        this.scanner = scanner;
        this.userService = userService;
        this.foodService = foodService;
        this.orderService = orderService;
    }
    
    public void showCustomerMenu() {
        System.out.println("1. View Menu");
        System.out.println("2. Place Order");
        System.out.println("3. View My Orders");
    }
    
    public void handleCustomerChoice(int choice, User currentUser) throws InvalidOrderException {
        switch (choice) {
            case 1:
                displayMenu();
                break;
            case 2:
                placeOrder(currentUser);
                break;
            case 3:
                viewMyOrders(currentUser);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    public void registerCustomer() {
        System.out.println("\n=== Customer Registration ===");
        
        String id = InputValidator.getValidInput(scanner, "Enter Customer ID (min 3 chars): ", 
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
        
        userService.registerCustomer(id, name, email, phone, password);
        System.out.println("Customer registered successfully!");
    }
    
    private void displayMenu() {
        System.out.println("\n=== Food Menu ===");
        Map<FoodItem, Integer> menu = foodService.getMenu();
        
        if (menu.isEmpty()) {
            System.out.println("No items available in the menu.");
            return;
        }
        
        System.out.printf("%-20s %-10s %-10s%n", "Item Name", "Price", "Quantity");
        System.out.println("----------------------------------------");
        
        for (Map.Entry<FoodItem, Integer> entry : menu.entrySet()) {
            FoodItem item = entry.getKey();
            int quantity = entry.getValue();
            System.out.printf("%-20s $%-9.2f %-10d%n", item.getName(), item.getPrice(), quantity);
        }
    }
    
    private void placeOrder(User currentUser) throws InvalidOrderException {
        // Check if delivery personnel are available before taking order
        List<User> availableDeliveryPersons = userService.getAvailableDeliveryPersons();
        if (availableDeliveryPersons.isEmpty()) {
            System.out.println("Sorry! No delivery personnel are available right now. Orders cannot be placed at this time.");
            System.out.println("Please try again later when delivery staff becomes available.");
            return;
        }
        
        displayMenu();
        
        Map<String, Integer> orderItems = new HashMap<>();
        
        System.out.println("\nEnter items to order (type 'done' to finish):");
        
        while (true) {
            System.out.print("Item name (or 'done'): ");
            String itemName = scanner.nextLine().trim();
            
            if ("done".equalsIgnoreCase(itemName)) {
                break;
            }
            
            if (foodService.getFoodItemByName(itemName) == null) {
                System.out.println("Item not found. Please try again.");
                continue;
            }
            
            int quantity;
            do {
                System.out.print("Quantity: ");
                quantity = getIntInput();
                if (!InputValidator.isValidQuantity(quantity)) {
                    System.out.println("Quantity must be greater than 0.");
                }
            } while (!InputValidator.isValidQuantity(quantity));
            
            orderItems.put(itemName, orderItems.getOrDefault(itemName, 0) + quantity);
        }
        
        if (orderItems.isEmpty()) {
            System.out.println("No items selected for order.");
            return;
        }
        
        Order order = orderService.placeOrder(currentUser.getId(), orderItems);
        System.out.println("\nOrder placed successfully!");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Total Amount: $" + String.format("%.2f", order.getTotalAmount()));
        System.out.println("Delivery Person ID: " + order.getDeliveryPersonId());
    }
    
    private void viewMyOrders(User currentUser) {
        List<Order> orders = orderService.getOrdersByCustomerId(currentUser.getId());
        
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        
        System.out.println("\n=== Your Orders ===");
        for (Order order : orders) {
            System.out.println(orderService.getOrderDetails(order.getOrderId()));
            System.out.println("------------------------");
        }
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