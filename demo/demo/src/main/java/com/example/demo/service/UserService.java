package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ReaderRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.demo.model.Reader;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ReaderRepository readerRepository;

    public User registerUser(UserDTO userDTO) {
        System.out.println(">>> Email: " + userDTO.getEmail());
        System.out.println(">>> Roles recibidos: " + userDTO.getRoles());

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setActive(true);

        List<Role> roles = userDTO.getRoles().stream()
            .map(this::getOrCreateRole)
            .toList();

        user.setRoles(new HashSet<>(roles));
        User savedUser = userRepository.save(user);

        System.out.println(">>> DEBUG: Revisión de roles asignados al usuario:");
        roles.forEach(role -> {
            System.out.println("  - role.getId(): " + role.getId());
            System.out.println("  - role.getName(): " + role.getName());
        });

        boolean esLector = roles.stream()
            .map(Role::getName)
            .filter(name -> name != null)
            .anyMatch(name -> name.equalsIgnoreCase("LECTOR"));

        if (esLector) {
            Reader reader = new Reader();
            reader.setUser(savedUser);
            reader.setEstado(true);
            readerRepository.save(reader);
            System.out.println(">>> Reader creado para: " + savedUser.getEmail());
        }

        return savedUser;
    }
    
        public User registerLector(UserDTO userDTO) {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setActive(true);

            Role lectorRole = roleRepository.findByName("LECTOR")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("LECTOR");
                    return roleRepository.save(newRole);
                });

            user.setRoles(Collections.singleton(lectorRole));
            User savedUser = userRepository.save(user);

            Reader reader = new Reader();
            reader.setUser(savedUser);
            readerRepository.save(reader);

            return savedUser;
        }

    private Role getOrCreateRole(String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isPresent()) {
            return roleOpt.get();
        } else {
            Role newRole = new Role();
            newRole.setName(roleName);
            return roleRepository.save(newRole);
        }
    }
}