package com.abes.foodDeliveryApplication.services;

import com.abes.foodDeliveryApplication.dto.User;
import com.abes.foodDeliveryApplication.exception.UserNotFoundException;
import com.abes.foodDeliveryApplication.exception.InvalidCredentialsException;
import java.util.List;

public interface UserService {
    void registerCustomer(String id, String name, String email, String phone, String password);
    void registerDeliveryPerson(String id, String name, String email, String phone, String password);
    User authenticateUser(String id, String password) throws UserNotFoundException, InvalidCredentialsException;
    User getUserById(String id) throws UserNotFoundException;
    List<User> getAllDeliveryPersons();
    List<User> getAvailableDeliveryPersons();
    void updateUserAvailability(String userId, boolean available) throws UserNotFoundException;
    void removeDeliveryPerson(String id) throws UserNotFoundException;
}
