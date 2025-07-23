package com.example.chatserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

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
}
