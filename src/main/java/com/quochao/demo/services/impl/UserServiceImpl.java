package com.quochao.demo.services.impl;

import com.quochao.demo.dtos.RegisterUserDTO;
import com.quochao.demo.dtos.UserDTO;
import com.quochao.demo.entities.Role;
import com.quochao.demo.entities.User;
import com.quochao.demo.exceptions.ResourceNotFoundException;
import com.quochao.demo.mappers.UserMapper;
import com.quochao.demo.repositories.RoleRepository;
import com.quochao.demo.repositories.UserRepository;
import com.quochao.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User register(User user) {
        Optional<Role> role = roleRepository.findById(1L);
        role.ifPresent(user::setRole);
        return userRepository.save(user);
    }

    @Override
    public UserDTO register(RegisterUserDTO registerUserDTO) {
        User user = UserMapper.getInstance().toEntity(registerUserDTO);
        Role role = roleRepository.findById(registerUserDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Not found user role"));
        user.setRole(role);
        return UserMapper.getInstance().toDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserMapper.getInstance().toDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
