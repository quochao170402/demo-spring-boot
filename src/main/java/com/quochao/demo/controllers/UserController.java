package com.quochao.demo.controllers;

import com.quochao.demo.dtos.RegisterUserDTO;
import com.quochao.demo.dtos.ResponseObjectDTO;
import com.quochao.demo.dtos.UserDTO;
import com.quochao.demo.entities.User;
import com.quochao.demo.exceptions.ResourceNotFoundException;
import com.quochao.demo.mappers.UserMapper;
import com.quochao.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseObjectDTO> register(@Valid @RequestBody RegisterUserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObjectDTO("SUCCESSFUL", "Register user successful",
                        userService.register(userDTO)));
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAll();
    }

    @GetMapping(path = "/{id}")
    public UserDTO getUser(@PathVariable Long id) {
//       using this pattern to check a string is a numeric : -?\d+(\.\d+)?
//        if (id == null || id.isEmpty() || !id.matches("-?\\d+(\\.\\d+)?"))
//            throw new ResourceNotFoundException("ID must numeric!!!");
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id " + id));
        return UserMapper.getInstance().toDTO(user);
    }
}
