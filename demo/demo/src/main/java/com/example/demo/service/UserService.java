package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setActive(true);

        // Busca el rol LECTOR o créalo si no existe
        Role lectorRole = getOrCreateRole("LECTOR");
        user.setRoles(Collections.singleton(lectorRole));

        return userRepository.save(user);
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