package com.quochao.demo.dtos;

import java.io.Serializable;

public class RegisterUserDTO implements Serializable {
    private String name;
    private String username;
    private String password;
    private Long roleId;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(String name, String username, String password, Long roleId) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
