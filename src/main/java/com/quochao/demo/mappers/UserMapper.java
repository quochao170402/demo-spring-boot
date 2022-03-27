package com.quochao.demo.mappers;

import com.quochao.demo.dtos.RegisterUserDTO;
import com.quochao.demo.dtos.UserDTO;
import com.quochao.demo.entities.User;

public class UserMapper {
    private static UserMapper INSTANCE;

//    Return  instance of user mapper to provide methods for mapping user to dto and vice versa
    public static UserMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserMapper();
        }
        return INSTANCE;
    }

//    Convert dto to entity
    public User toEntity(RegisterUserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());;
        user.setPassword(dto.getPassword());
        return user;
    }

//    Convert entity to dto
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setRole(RoleMapper.getInstance().toDTO(user.getRole()));
        return dto;
    }
}
