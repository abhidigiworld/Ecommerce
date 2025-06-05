package com.ecommerce.userservice.service;

import com.ecommerce.userservice.client.LoggingClient;
import com.ecommerce.userservice.dto.UserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.exception.DeletionNotAllowedException;
import com.ecommerce.userservice.exception.DuplicateEmailException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImplementation implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoggingClient loggingClient;

    @Override
    public UserResponse createUser(UserRequest userRequest) throws DuplicateEmailException {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            String errorMessage = userRequest.getEmail() + " Already Exists";
            loggingClient.logMessage("WARN", errorMessage);
            throw new DuplicateEmailException(errorMessage);
        }

        User newUser = User.builder()
                .name(userRequest.getName()).email(userRequest.getEmail()).password(userRequest.getPassword())
                .shippingAddress(userRequest.getShippingAddress())
                .paymentDetails(userRequest.getPaymentDetails())
                .isDeleted(false)
                .role("ROLE_USER")
                .build();

        User savedUser = userRepository.save(newUser);
        loggingClient.logMessage("INFO", "User created with ID: " + savedUser.getUserId());
        return convertToResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToResponse).toList();
    }

    @Override
    public UserResponse getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " Not Found!"));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " Not Found!"));

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setShippingAddress(userRequest.getShippingAddress());
        user.setPaymentDetails(userRequest.getPaymentDetails());

        User updatedUser = userRepository.save(user);
        loggingClient.logMessage("INFO", "User with ID: " + id + " updated successfully");
        return convertToResponse(updatedUser);
    }

    @Override
    public UserResponse softDeleteUser(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " Not Found!"));

        user.setIsDeleted(true);
        User updatedUser = userRepository.save(user);
        loggingClient.logMessage("INFO", "User with ID: " + id + " marked as soft deleted");
        return convertToResponse(updatedUser);
    }

    @Override
    public UserResponse hardDeleteUser(Long id) throws UserNotFoundException, DeletionNotAllowedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " Not Found!"));

        if (!user.getIsDeleted()) {
            throw new DeletionNotAllowedException("Deletion is not allowed for User Name: " + user.getName());
        }

        userRepository.delete(user);
        return convertToResponse(user);
    }

    @Override
    public String updateUserRole(Long id, String newRole) throws UserNotFoundException {
        List<String> allowedRoles = List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_SUBADMIN");
        if (!allowedRoles.contains(newRole)) {
            return "Invalid role: " + newRole + ". Allowed roles are: ROLE_USER, ROLE_ADMIN, ROLE_SUBADMIN.";
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setRole(newRole);
        userRepository.save(user);
        return "Role updated to: " + newRole;
    }

    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .shippingAddress(user.getShippingAddress())
                .paymentDetails(user.getPaymentDetails())
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
