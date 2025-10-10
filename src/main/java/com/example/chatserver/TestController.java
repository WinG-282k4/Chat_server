package com.example.chatserver;

import com.example.chatserver.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final UserRepository userRepository;

    // Yêu cầu Spring inject UserRepository vào đây
    @Autowired
    public TestController(UserRepository userRepository) {
        // Nếu Spring tìm thấy UserRepository, dòng này sẽ được in ra khi khởi động
        System.out.println("======================================================");
        System.out.println("SUCCESS: UserRepository has been injected into TestController!");
        System.out.println("======================================================");
        this.userRepository = userRepository;
    }

    @GetMapping("/test-repo")
    public ResponseEntity<String> testRepo() {
        try {
            long count = userRepository.count();
            return ResponseEntity.ok("UserRepository is working! User count: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error using UserRepository: " + e.getMessage());
        }
    }
}