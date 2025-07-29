package com.example.chatserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    // This service can be used to handle user-related business logic
    // For example, methods to create, update, delete users, etc.

    // Example method to create a user
    public UserDTO createUser(UserDTO userDto) {
        // Logic to create a user

        //Check if user already exists
        if(userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            System.out.println("[UserService] Password is null or empty: '" + userDto.getPassword() + "'");
            throw new RuntimeException("Password cannot be empty");
        }

        User user = userMapper.toEntity(userDto);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setVerify(false);
        userRepository.save(user);

        return userMapper.toDto(user); // Placeholder return
    }

    // Example method to find a user by ID
    public UserDTO findUserById(Long userId) {
        return null;
    }
}
