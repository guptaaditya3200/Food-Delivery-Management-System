package com.abes.foodDeliveryApplication.services;

import com.abes.foodDeliveryApplication.exception.*;

public interface ManagerService {
    void addNewFoodItem(String name, double price, int quantity);
    void restockFoodItem(String itemName, int quantity);
    void removeDeliveryPerson(String deliveryPersonId) throws UserNotFoundException;
    boolean validateManagerRole(String managerId) throws UserNotFoundException;
}
