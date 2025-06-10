package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("LECTOR");
    }

    private void createRoleIfNotExists(String roleName) {
        roleRepository.findByName(roleName).ifPresentOrElse(
            r -> {}, // Ya existe
            () -> {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Rol creado: " + roleName);
            }
        );
    }
}