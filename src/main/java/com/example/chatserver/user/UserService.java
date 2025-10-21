package com.example.chatserver.user;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private static final Log log = LogFactory.getLog(UserService.class);
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;
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
        User existingUser = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
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
        User existingUser = userRepository.findByUsername(sub)
                .orElseThrow(() -> new RuntimeException("User not found"));
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

    public void connect(User user) {
        // Logic to handle user connection
        var existingUser = userRepository.findByUsername(user.getUsername())
                .orElse(null);
        if (existingUser != null) {
            log.debug("[UserService] Setting user status to ONLINE for user: " + user.getUsername());
            existingUser.setStatus(Status.ONLINE);
            userRepository.save(existingUser);
        } else {
            log.debug("[UserService] User not found for connection: " + user.getUsername());
            return;
        }
    }

    public void disconnect(User user) {
        // Logic to handle user disconnection
        var existingUser = userRepository.findByUsername(user.getUsername())
                .orElse(null);
        if (existingUser != null) {
            log.debug("[UserService] Setting user status to OFFLINE for user: " + user.getUsername());
            existingUser.setStatus(Status.OFFLINE);
            userRepository.save(existingUser);
        } else {
            log.debug("[UserService] User not found for disconnection: " + user.getUsername());
            return;
        }
    }

    public List<User> findConnectedUsers() {
        // Logic to find connected users
        return userRepository.findAllByStatus(Status.ONLINE);
    }
}

