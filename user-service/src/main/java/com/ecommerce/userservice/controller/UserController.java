package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.UserRequest;

import com.ecommerce.userservice.dto.UserResponse;

import com.ecommerce.userservice.exception.DeletionNotAllowedException;
import com.ecommerce.userservice.exception.DuplicateEmailException;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) throws DuplicateEmailException {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) throws UserNotFoundException {
        return new ResponseEntity<>(userService.updateUser(id, userRequest), HttpStatus.OK);
    }
    
    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<UserResponse> softDeleteUser(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.softDeleteUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/hard-delete")
    public ResponseEntity<UserResponse> hardDeleteUser(@PathVariable Long id) throws UserNotFoundException, DeletionNotAllowedException {
        return new ResponseEntity<>(userService.hardDeleteUser(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/update-role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestParam String newRole) throws UserNotFoundException {
        return new ResponseEntity<>(userService.updateUserRole(id, newRole), HttpStatus.OK);
    }

}
