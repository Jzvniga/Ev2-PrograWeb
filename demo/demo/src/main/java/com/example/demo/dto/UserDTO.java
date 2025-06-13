package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private String email;
    private String password;
    private List<String> roles;

    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}