package com.quochao.demo.services;

import com.quochao.demo.dtos.RegisterUserDTO;
import com.quochao.demo.dtos.UserDTO;
import com.quochao.demo.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);
    UserDTO register(RegisterUserDTO registerUserDTO);
    List<UserDTO> findAll();

    User getById(Long id);
    Optional<User> findByUsername(String username);

    Optional<User> findById(long id);
}
