package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.UserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.exception.DeletionNotAllowedException;
import com.ecommerce.userservice.exception.DuplicateEmailException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import java.util.List;

public interface IUserService {
    UserResponse createUser(UserRequest userRequest) throws DuplicateEmailException;
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id) throws UserNotFoundException;
    UserResponse updateUser(Long id, UserRequest userRequest) throws UserNotFoundException;
    UserResponse softDeleteUser(Long id) throws UserNotFoundException;
    UserResponse hardDeleteUser(Long id) throws UserNotFoundException, DeletionNotAllowedException;
    String updateUserRole(Long id, String newRole) throws UserNotFoundException;
}
