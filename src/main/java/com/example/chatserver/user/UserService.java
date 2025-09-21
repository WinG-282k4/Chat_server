package com.example.chatserver.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserService {

    private static final Log log = LogFactory.getLog(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    // This service can be used to handle user-related business logic
    // For example, methods to create, update, delete users, etc.

    // Example method to create a user
//    public UserDTO createUser(UserDTO userDto) {
//        // Logic to create a user
//
//        //Check if user already exists
//        if(userRepository.existsByUsername(userDto.getUsername())) {
//            throw new RuntimeException("User already exists");
//        }
//
//        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
//            System.out.println("[UserService] Password is null or empty: '" + userDto.getPassword() + "'");
//            throw new RuntimeException("Password cannot be empty");
//        }
//
//        User user = userMapper.toEntity(userDto);
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//        user.setVerify(false);
//        userRepository.save(user);
//
//        return userMapper.toDto(user); // Placeholder return
//    }

    // Example method to find a user by ID
    public UserDTO findUserById(Long userId) {
        return null;
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // Update fields if they are provided in the DTO
        if (userDTO.getName() != null) {
            existingUser.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getVerify() != null) {
            existingUser.setVerify(userDTO.getVerify());
        }

        userRepository.save(existingUser);
        return new UserDTO().builder()
                .name(existingUser.getName()
                ).email(existingUser.getEmail()
                ).username(existingUser.getUsername()
                ).verify(existingUser.getVerify())
                .build();
    }

    public boolean changePassword(String sub, String oldPassword, String newPassword) {
        User existingUser = userRepository.findByUsername(sub);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            log.debug("[UserService] Old password does not match for user: " + sub);
            return false; // Old password does not match
        }

        if (newPassword == null || newPassword.isEmpty()) {
            log.debug("[UserService] New password is null or empty for user: " + sub);
            throw new RuntimeException("New password cannot be empty");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
        return true;
    }
}

