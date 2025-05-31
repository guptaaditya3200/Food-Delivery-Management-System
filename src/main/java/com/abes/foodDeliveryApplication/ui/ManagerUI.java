package com.abes.foodDeliveryApplication.ui;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.dto.FoodItem;
import com.abes.foodDeliveryApplication.services.*;
import com.abes.foodDeliveryApplication.exception.*;
import com.abes.foodDeliveryApplication.utils.InputValidator;
import com.abes.foodDeliveryApplication.exception.UserNotFoundException;
import com.abes.foodDeliveryApplication.services.FoodService;
import com.abes.foodDeliveryApplication.services.ManagerService;
import com.abes.foodDeliveryApplication.services.UserService;

import java.util.*;

public class ManagerUI {
    private final Scanner scanner;
    private final UserService userService;
    private final FoodService foodService;
    private final ManagerService managerService;
    
    public ManagerUI(Scanner scanner, UserService userService, FoodService foodService, ManagerService managerService) {
        this.scanner = scanner;
        this.userService = userService;
        this.foodService = foodService;
        this.managerService = managerService;
    }
    
    public void showManagerMenu() {
        System.out.println("1. View Menu");
        System.out.println("2. Add New Food Item");
        System.out.println("3. Restock Food Item");
        System.out.println("4. View All Delivery Personnel");
        System.out.println("5. Remove Delivery Person");
    }
    
    public void handleManagerChoice(int choice) throws UserNotFoundException {
        switch (choice) {
            case 1:
                displayMenu();
                break;
            case 2:
                addNewFoodItem();
                break;
            case 3:
                restockFoodItem();
                break;
            case 4:
                viewAllDeliveryPersonnel();
                break;
            case 5:
                removeDeliveryPerson();
                break;
            default:
                System.out.println("Invalid choice.");
        }
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
    
    private void addNewFoodItem() {
        System.out.println("\n=== Add New Food Item ===");
        
        String name = InputValidator.getValidInput(scanner, "Enter item name: ", "Name cannot be empty.");
        
        double price;
        do {
            System.out.print("Enter price: $");
            price = getDoubleInput();
            if (!InputValidator.isValidPrice(price)) {
                System.out.println("Price must be greater than 0.");
            }
        } while (!InputValidator.isValidPrice(price));
        
        int quantity;
        do {
            System.out.print("Enter initial quantity: ");
            quantity = getIntInput();
            if (!InputValidator.isValidQuantity(quantity)) {
                System.out.println("Quantity must be greater than 0.");
            }
        } while (!InputValidator.isValidQuantity(quantity));
        
        managerService.addNewFoodItem(name, price, quantity);
        System.out.println("Food item added successfully!");
    }
    
    private void restockFoodItem() {
        displayMenu();
        
        String itemName = InputValidator.getValidInput(scanner, "Enter item name to restock: ", 
                                                     "Item name cannot be empty.");
        
        int quantity;
        do {
            System.out.print("Enter quantity to add: ");
            quantity = getIntInput();
            if (!InputValidator.isValidQuantity(quantity)) {
                System.out.println("Quantity must be greater than 0.");
            }
        } while (!InputValidator.isValidQuantity(quantity));
        
        managerService.restockFoodItem(itemName, quantity);
        System.out.println("Item restocked successfully!");
    }
    
    private void viewAllDeliveryPersonnel() {
        List<User> deliveryPersons = userService.getAllDeliveryPersons();
        
        if (deliveryPersons.isEmpty()) {
            System.out.println("No delivery personnel registered.");
            return;
        }
        
        System.out.println("\n=== All Delivery Personnel ===");
        System.out.printf("%-10s %-20s %-15s %-12s%n", "ID", "Name", "Phone", "Available");
        System.out.println("-------------------------------------------------------");
        
        for (User person : deliveryPersons) {
            System.out.printf("%-10s %-20s %-15s %-12s%n", 
                            person.getId(), person.getName(), person.getPhone(), 
                            person.isAvailable() ? "Yes" : "No");
        }
    }
    
    private void removeDeliveryPerson() throws UserNotFoundException {
        viewAllDeliveryPersonnel();
        
        String id = InputValidator.getValidInput(scanner, "Enter Delivery Person ID to remove: ", 
                                               "ID cannot be empty.");
        
        managerService.removeDeliveryPerson(id);
        System.out.println("Delivery person removed successfully!");
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
    
    private double getDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}