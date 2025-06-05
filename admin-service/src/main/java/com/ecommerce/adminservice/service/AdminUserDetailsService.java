package com.ecommerce.adminservice.service;

import com.ecommerce.adminservice.entity.AdminUser;
import com.ecommerce.adminservice.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminUserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    public Optional<AdminUser> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    public AdminUser save(AdminUser adminUser) {
        return adminUserRepository.save(adminUser);
    }

    public Optional<AdminUser> findById(Long id) {
        return adminUserRepository.findById(id);
    }

    public void delete(AdminUser adminUser) {
        adminUserRepository.delete(adminUser);
    }

    public void deleteById(Long id) {
        adminUserRepository.deleteById(id);
    }
}