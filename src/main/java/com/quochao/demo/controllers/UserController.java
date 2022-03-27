package com.quochao.demo.controllers;

import com.quochao.demo.dtos.RegisterUserDTO;
import com.quochao.demo.dtos.UserDTO;
import com.quochao.demo.entities.User;
import com.quochao.demo.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDTO register(@RequestBody RegisterUserDTO userDTO) {
        return userService.register(userDTO);
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }
}
