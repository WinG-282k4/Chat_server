package com.example.chatserver.user;

import com.example.chatserver.common.ApiResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Log log = LogFactory.getLog(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository Repository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public List<UserDTO> getUsers() {
        return Repository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @PostMapping("/register")
    public ApiResponse createUser(@RequestBody UserDTO userDto) {
        // Logic to create a user
        log.debug("[UserController] Password from request: " + userDto.getPassword());
        return new ApiResponse("success","User created successfully", userService.createUser(userDto));
    }
}
