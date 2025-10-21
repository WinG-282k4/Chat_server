package com.example.chatserver.authentication;

import com.example.chatserver.security.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.Payload;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import java.util.Map;

@RestController()
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Log log = LogFactory.getLog(AuthenticationController.class);

    @Autowired
    private  AuthenticationService authenticationService;
    @Autowired
    private View error;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Authentication authentication) {
        // Logic to create a user
        log.debug("[UserController] Password from request: " + authentication.getPassword());
        return ResponseEntity.ok(authenticationService.createUser(authentication));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Authentication authentication) {
        log.debug("[UserController] Password from request: " + authentication.getPassword());
        try {
            // Gọi service để login
            var authResponse = authenticationService.Login(authentication);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            // Khi password sai hoặc user không tồn tại
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "Status", "error",
                            "message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        // Lấy JTI và EXP từ request attributes mà filter đã set
        String jti = (String) request.getAttribute("jwtJti");
        Long exp = (Long) request.getAttribute("jwtExp");

        if (jti != null && exp != null) {
            // Chuyển đổi exp (milliseconds) sang đối tượng Date nếu cần
            // Hoặc đảm bảo service của bạn chấp nhận Long
            authenticationService.logout(jti, exp); // service nhận (String, Long)
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Logged out successfully"
        ));
    }


    @PostMapping("/introspect")
    public ResponseEntity introspect(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // Spring Security sẽ tự động lấy user từ SecurityContext cho bạn
        // Nếu userPrincipal là null (token không hợp lệ), Spring sẽ chặn request từ trước

        String username = null;
        if (userPrincipal != null) {
            username = userPrincipal.getUsername(); // Lấy username một cách an toàn
        }

        return ResponseEntity.ok(Map.of(
                "active", username != null,
                "username", username
        ));
    }

}
