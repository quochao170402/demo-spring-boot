package com.quochao.demo.dtos;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String name;
    private String username;
    private String password;
    private RoleDTO role;

    public UserDTO() {
    }

    public UserDTO(String name, String username, String password, RoleDTO role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
