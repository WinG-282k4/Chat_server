package com.example.chatserver.authentication;

import com.example.chatserver.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Log log = LogFactory.getLog(AuthenticationController.class);

    @Autowired
    private  AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse register(@RequestBody Authentication authentication) {
        // Logic to create a user
        log.debug("[UserController] Password from request: " + authentication.getPassword());
        return new ApiResponse("success","User created successfully", authenticationService.createUser(authentication));
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody Authentication authentication) {
        // Logic to create a user
        log.debug("[UserController] Password from request: " + authentication.getPassword());
        return new ApiResponse("success","User logged in successfully", authenticationService.Login(authentication));
    }

    @PostMapping("/introspect")
    public ApiResponse introspect(HttpServletRequest request) {
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
                return new ApiResponse("error", "Invalid payload format", null);
            }
        }
        return new ApiResponse("success", "Token introspected successfully", sub);
    }
}
