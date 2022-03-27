package com.quochao.demo.services;

import com.quochao.demo.dtos.RegisterUserDTO;
import com.quochao.demo.dtos.UserDTO;
import com.quochao.demo.entities.User;

import java.util.List;

public interface UserService {
    UserDTO register(RegisterUserDTO dto);
    List<UserDTO> findAll();

    User getById(Long id);
}
