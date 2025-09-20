package com.example.chatserver.authentication;

import com.example.chatserver.common.ApiResponse;
import com.example.chatserver.user.UserDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Log log = LogFactory.getLog(AuthenticationController.class);

    @Autowired
    private  AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody Authentication authentication) {
        // Logic to create a user
        log.debug("[UserController] Password from request: " + authentication.getPassword());
        return new ApiResponse("success","User logged in successfully", authenticationService.Login(authentication));
    }

    @PostMapping("/introspect")
    public ApiResponse introspect(@RequestBody TokenRequest tokenRequest) {
        String token = tokenRequest.getToken();
        // Logic to create a user
        log.debug("[AuthenticationController] Token from request: " + token);
        return new ApiResponse("success","Token introspected successfully", authenticationService.introspectToken(token));
    }
}
