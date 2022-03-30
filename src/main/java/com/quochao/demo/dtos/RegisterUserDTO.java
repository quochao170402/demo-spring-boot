package com.quochao.demo.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegisterUserDTO implements Serializable {
    @NotNull(message = "Name of user cannot be null")
    private String name;

    @NotNull(message = "Username cannot not null")
    @Size(min = 6, max = 30, message = "The username must be greater than 6 characters and less than 30 characters")
    private String username;

    @NotNull(message = "Password cannot not null")
    @Size(min = 6, max = 30, message = "The password must be greater than 6 characters and less than 30 characters")
    private String password;

    private Long roleId;

    public RegisterUserDTO() {
        roleId = 1L;
    }

    public RegisterUserDTO(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.roleId = 1L;
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
