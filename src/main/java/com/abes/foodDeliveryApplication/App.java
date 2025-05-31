package com.abes.foodDeliveryApplication;
import com.abes.foodDeliveryApplication.services.FoodService;
import com.abes.foodDeliveryApplication.utils.*;
import com.abes.foodDeliveryApplication.services.FoodServiceImpl;
import com.abes.foodDeliveryApplication.ui.MenuHandler;

/**
 * Hello world!
 *
 */
public class App 
{
     public static void main(String[] args) {
        // Initialize the system with some sample data
    	 CollectionUtil.initializeSampleData();
        
        // Start the application
        MenuHandler menuHandler = new MenuHandler();
        menuHandler.start();
    }
}
