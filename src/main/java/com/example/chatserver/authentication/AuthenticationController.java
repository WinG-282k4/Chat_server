package com.example.chatserver.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.Payload;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        // Logic to handle user logout
        // Get jti from request attribute set by JwtFilter
        String Spayload = request.getAttribute("jwtPayload") != null ? (String) request.getAttribute("jwtPayload") : null;
        Payload jwtPayload = new Payload(Spayload);
        String jti = jwtPayload.toJSONObject().get("jti").toString();
        Long exp = jwtPayload.toJSONObject().get("exp") != null ? Long.parseLong(jwtPayload.toJSONObject().get("exp").toString()) : null;
        if (jti != null && exp != null) {
            authenticationService.logout(jti, exp);
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Logged out successfully"
        ));
    }


    @PostMapping("/introspect")
    public ResponseEntity introspect(HttpServletRequest request) {
        String payload = request.getAttribute("jwtPayload") != null ? (String) request.getAttribute("jwtPayload") : null;
        System.out.println(payload);
        String sub = null;
        if (payload != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> json = mapper.readValue(payload, Map.class);
                sub = (String) json.get("sub");
                System.out.println(json.get("sub"));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok(Map.of(
                "active", sub != null,
                "username", sub
        ));
    }


}
